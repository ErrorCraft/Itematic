package net.errorcraft.itematic.mixin.predicate.item;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemPredicate.class)
public class ItemPredicateExtender {
    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getEntryCodec()Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<RegistryEntry<Item>> doNotUseStaticItemRegistry(DefaultedRegistry<Item> instance) {
        return RegistryFixedCodec.of(RegistryKeys.ITEM);
    }
}
