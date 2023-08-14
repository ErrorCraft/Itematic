package net.errorcraft.itematic.mixin.loot.entry;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemEntry.class)
public class ItemEntryExtender {
    @Redirect(
        method = "method_53286",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;createEntryCodec()Lcom/mojang/serialization/Codec;"
        ),
        remap = false
    )
    private static Codec<RegistryEntry<Item>> doNotUseStaticRegistry(DefaultedRegistry<Item> instance) {
        return RegistryFixedCodec.of(RegistryKeys.ITEM);
    }
}
