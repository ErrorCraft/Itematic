package net.errorcraft.itematic.mixin.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(BuiltInRegistries.class)
public class RegistriesExtender {
    @Inject(
        method = "internalRegister(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/WritableRegistry;Lnet/minecraft/core/registries/BuiltInRegistries$RegistryBootstrap;)Lnet/minecraft/core/WritableRegistry;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static <T, R extends WritableRegistry<T>> void doNotAddSpecificRegistries(ResourceKey<? extends Registry<T>> key, R registry, BuiltInRegistries.RegistryBootstrap<T> initializer, CallbackInfoReturnable<R> info) {
        if (Objects.equals(key, Registries.ITEM)) {
            info.setReturnValue(registry);
        }
    }
}
