package net.errorcraft.itematic.data.server.registry;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryWrapper;

@SuppressWarnings("UnstableApiUsage")
public class DynamicRegistryProviderUtil {
    private DynamicRegistryProviderUtil() {}

    public static <T> void addAll(FabricDynamicRegistryProvider.Entries entries, RegistryWrapper.Impl<T> registry) {
        registry.streamKeys().forEach(key -> entries.add(registry, key));
    }
}
