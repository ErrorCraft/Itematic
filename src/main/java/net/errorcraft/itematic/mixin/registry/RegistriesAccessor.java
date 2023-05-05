package net.errorcraft.itematic.mixin.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Registries.class)
public interface RegistriesAccessor {
    @Invoker("create")
    static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Registries.Initializer<T> initializer) {
        throw new AssertionError();
    }
}
