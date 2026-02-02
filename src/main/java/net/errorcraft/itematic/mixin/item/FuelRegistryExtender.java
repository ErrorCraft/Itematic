package net.errorcraft.itematic.mixin.item;

import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.Predicate;

public class FuelRegistryExtender {
    @Mixin(FuelRegistry.Builder.class)
    public static class BuilderExtender {
        @Redirect(
            method = "remove",
            at = @At(
                value = "INVOKE",
                target = "Lit/unimi/dsi/fastutil/objects/ObjectSortedSet;removeIf(Ljava/util/function/Predicate;)Z",
                remap = false
            )
        )
        private boolean doNotRemove(ObjectSortedSet<Item> instance, Predicate<Item> predicate) {
            return false;
        }

        @Redirect(
            method = "add(Lnet/minecraft/registry/tag/TagKey;I)Lnet/minecraft/item/FuelRegistry$Builder;",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/registry/RegistryWrapper;getOptional(Lnet/minecraft/registry/tag/TagKey;)Ljava/util/Optional;"
            )
        )
        private Optional<RegistryEntryList.Named<Item>> doNotGet(RegistryWrapper<Item> instance, TagKey<Item> tagKey) {
            return Optional.empty();
        }

        @Redirect(
            method = "add(ILnet/minecraft/item/Item;)V",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/Item;isEnabled(Lnet/minecraft/resource/featuretoggle/FeatureSet;)Z"
            )
        )
        private boolean doNotAdd(Item instance, FeatureSet featureSet) {
            return false;
        }
    }
}
