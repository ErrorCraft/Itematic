package net.errorcraft.itematic.mixin.item;

import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.FuelValues;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.Predicate;

public class FuelRegistryExtender {
    @Mixin(FuelValues.Builder.class)
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
            method = "add(Lnet/minecraft/tags/TagKey;I)Lnet/minecraft/world/level/block/entity/FuelValues$Builder;",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/core/HolderLookup;get(Lnet/minecraft/tags/TagKey;)Ljava/util/Optional;"
            )
        )
        private Optional<HolderSet.Named<Item>> doNotGet(HolderLookup<Item> instance, TagKey<Item> tagKey) {
            return Optional.empty();
        }

        @Redirect(
            method = "putInternal(ILnet/minecraft/world/item/Item;)V",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/Item;isEnabled(Lnet/minecraft/world/flag/FeatureFlagSet;)Z"
            )
        )
        private boolean doNotAdd(Item instance, FeatureFlagSet featureSet) {
            return false;
        }
    }
}
