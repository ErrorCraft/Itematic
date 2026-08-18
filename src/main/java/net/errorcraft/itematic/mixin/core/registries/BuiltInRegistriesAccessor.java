package net.errorcraft.itematic.mixin.core.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BuiltInRegistries.class)
public interface BuiltInRegistriesAccessor {
    @Invoker("registerSimple")
    static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> key, BuiltInRegistries.RegistryBootstrap<T> initializer) {
        throw new AssertionError();
    }
}
