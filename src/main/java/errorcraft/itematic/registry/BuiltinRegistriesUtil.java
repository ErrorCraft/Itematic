package errorcraft.itematic.registry;

import com.mojang.serialization.Lifecycle;
import errorcraft.itematic.mixin.util.registry.BuiltinRegistriesAccessor;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class BuiltinRegistriesUtil {
    public static <T> DefaultedRegistry<T> addDefaultedRegistry(String defaultId, RegistryKey<Registry<T>> registryRef, BuiltinRegistries.Initializer<T> initializer) {
        return addDefaultedRegistry(defaultId, registryRef, initializer, Lifecycle.stable());
    }

    private static <T> DefaultedRegistry<T> addDefaultedRegistry(String defaultId, RegistryKey<Registry<T>> registryRef, BuiltinRegistries.Initializer<T> initializer, Lifecycle lifecycle) {
        DefaultedRegistry<T> reg = new DefaultedRegistry<>(defaultId, registryRef, lifecycle, false);
        return BuiltinRegistriesAccessor.addRegistry(registryRef, reg, initializer, lifecycle);
    }
}
