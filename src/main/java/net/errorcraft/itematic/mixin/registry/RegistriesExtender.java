package net.errorcraft.itematic.mixin.registry;

import net.minecraft.registry.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Registries.class)
public class RegistriesExtender {
    @Inject(
        method = "create(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lnet/minecraft/registry/Registries$Initializer;)Lnet/minecraft/registry/MutableRegistry;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static <T, R extends MutableRegistry<T>> void doNotAddSpecificRegistries(RegistryKey<? extends Registry<T>> key, R registry, Registries.Initializer<T> initializer, CallbackInfoReturnable<R> info) {
        if (Objects.equals(key, RegistryKeys.ITEM)) {
            info.setReturnValue(registry);
        }
    }
}
