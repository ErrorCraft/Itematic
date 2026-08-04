package net.errorcraft.itematic.mixin.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BuiltInRegistries.class)
public interface RegistriesAccessor {
    @Invoker("registerSimple")
    static <T> Registry<T> create(ResourceKey<? extends Registry<T>> key, BuiltInRegistries.RegistryBootstrap<T> initializer) {
        throw new AssertionError();
    }
}
