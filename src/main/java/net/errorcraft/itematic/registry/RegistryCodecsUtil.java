package net.errorcraft.itematic.registry;

import com.mojang.serialization.Codec;
import net.minecraft.registry.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RegistryCodecsUtil {
    private static Map<RegistryKey<? extends Registry<?>>, MutableRegistry<?>> dynamicRegistries;

    private RegistryCodecsUtil() {}

    public static MutableRegistry<?> registry(RegistryKey<?> key) {
        return dynamicRegistries.get(key);
    }

    public static void setDynamicRegistries(Map<RegistryKey<? extends Registry<?>>, MutableRegistry<?>> dynamicRegistries) {
        RegistryCodecsUtil.dynamicRegistries = dynamicRegistries;
    }

    public static RegistryOps.RegistryInfoGetter createInfoGetter(DynamicRegistryManager baseRegistryManager, Collection<MutableRegistry<?>> additionalRegistries) {
        return new MapRegistryInfoGetter(baseRegistryManager, additionalRegistries);
    }

    public record Entry(RegistryKey<? extends Registry<?>> key, Codec<?> codec) {
        @SuppressWarnings({"rawtypes", "unchecked"})
        public RegistryLoader.Entry<?> toLoaderEntry() {
            return new RegistryLoader.Entry(this.key, this.codec);
        }
    }

    private static class MapRegistryInfoGetter implements RegistryOps.RegistryInfoGetter {
        private final Map<RegistryKey<? extends Registry<?>>, RegistryOps.RegistryInfo<?>> keyToRegistryInfo;

        private MapRegistryInfoGetter(DynamicRegistryManager baseRegistryManager, Collection<MutableRegistry<?>> additionalRegistries) {
            this.keyToRegistryInfo = new HashMap<>();
            baseRegistryManager.streamAllRegistries().forEach(entry -> keyToRegistryInfo.put(entry.key(), registryInfo(entry.value())));
            additionalRegistries.forEach(registry -> keyToRegistryInfo.put(registry.getKey(), registryInfo(registry)));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> Optional<RegistryOps.RegistryInfo<T>> getRegistryInfo(RegistryKey<? extends Registry<? extends T>> registryRef) {
            return Optional.ofNullable((RegistryOps.RegistryInfo<T>) keyToRegistryInfo.get(registryRef));
        }

        private static <T> RegistryOps.RegistryInfo<T> registryInfo(MutableRegistry<T> registry) {
            return new RegistryOps.RegistryInfo<>(registry.getReadOnlyWrapper(), registry.createMutableEntryLookup(), registry.getLifecycle());
        }

        private static <T> RegistryOps.RegistryInfo<T> registryInfo(Registry<T> registry) {
            return new RegistryOps.RegistryInfo<>(registry.getReadOnlyWrapper(), registry.getTagCreatingWrapper(), registry.getLifecycle());
        }
    }
}
