package net.errorcraft.itematic.data.server.registry;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

public class DynamicRegistryProviderUtil {
    private DynamicRegistryProviderUtil() {}

    public static <T> void addAll(FabricDynamicRegistryProvider.Entries entries, HolderLookup.RegistryLookup<T> registry) {
        registry.listElementIds().forEach(key -> entries.add(registry, key));
    }
}
