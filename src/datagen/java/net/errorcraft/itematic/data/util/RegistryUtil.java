package net.errorcraft.itematic.data.util;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

public class RegistryUtil {
    private RegistryUtil() {}

    public static <T> void addAll(FabricDynamicRegistryProvider.Entries entries, HolderLookup.RegistryLookup<T> registry) {
        registry.listElementIds().forEach(key -> entries.add(registry, key));
    }
}
