package net.errorcraft.itematic.mixin.registry;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleRegistry.class)
public class SimpleRegistryExtender<T> {
    @Inject(
        method = "set",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/ObjectList;set(ILjava/lang/Object;)Ljava/lang/Object;",
            remap = false
        )
    )
    private void setRegistryEntryRawId(int i, RegistryKey<T> registryKey, T object, Lifecycle lifecycle, CallbackInfoReturnable<RegistryEntry.Reference<T>> info, @Local RegistryEntry.Reference<T> reference) {
        reference.itematic$setRawId(i);
    }
}
