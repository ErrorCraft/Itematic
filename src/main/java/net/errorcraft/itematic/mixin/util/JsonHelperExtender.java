package net.errorcraft.itematic.mixin.util;

import net.errorcraft.itematic.server.ServerAdvancementLoaderUtil;
import net.minecraft.item.Item;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(JsonHelper.class)
public class JsonHelperExtender {
    @Redirect(
        method = "asItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getEntry(Lnet/minecraft/registry/RegistryKey;)Ljava/util/Optional;"
        )
    )
    private static Optional<RegistryEntry.Reference<Item>> asItemUseDynamicRegistry(DefaultedRegistry<Item> instance, RegistryKey<Item> registryKey) {
        return ServerAdvancementLoaderUtil.getRegistryManager().get(RegistryKeys.ITEM).getEntry(registryKey);
    }
}
