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
        method = "add",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/ObjectList;add(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private void setRegistryEntryRawId(RegistryKey<T> key, T value, Lifecycle lifecycle, CallbackInfoReturnable<RegistryEntry.Reference<T>> info, @Local RegistryEntry.Reference<T> reference, @Local int rawId) {
        reference.itematic$setRawId(rawId);
    }
}
