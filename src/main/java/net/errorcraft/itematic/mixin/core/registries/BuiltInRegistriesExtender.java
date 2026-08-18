package net.errorcraft.itematic.mixin.core.registries;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Objects;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesExtender {
    @WrapMethod(
        method = "internalRegister(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/WritableRegistry;Lnet/minecraft/core/registries/BuiltInRegistries$RegistryBootstrap;)Lnet/minecraft/core/WritableRegistry;"
    )
    private static <T, R extends WritableRegistry<T>> R doNotAddSpecificRegistries(ResourceKey<? extends Registry<T>> name, R registry, BuiltInRegistries.RegistryBootstrap<T> loader, Operation<R> original) {
        if (Objects.equals(name, Registries.ITEM)) {
            return registry;
        }

        return original.call(name, registry, loader);
    }
}
