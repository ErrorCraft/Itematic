package net.errorcraft.itematic.mixin.registry;

import com.mojang.serialization.Lifecycle;
import net.errorcraft.itematic.registry.RegistryCodecsUtil;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RegistryCodecs.class)
public class RegistryCodecsExtender {
    @Redirect(
        method = "method_40345",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/registry/RegistryKey;Lcom/mojang/serialization/Lifecycle;)Lnet/minecraft/registry/SimpleRegistry;"
        )
    )
    private static SimpleRegistry<?> usePreviouslyCreatedRegistry(RegistryKey<?> key, Lifecycle lifecycle) {
        return (SimpleRegistry<?>) RegistryCodecsUtil.registry(key);
    }
}
