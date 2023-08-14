package net.errorcraft.itematic.loot;

import net.minecraft.registry.DynamicRegistryManager;

public class LootManagerUtil {
    private static DynamicRegistryManager.Immutable registryManager;

    private LootManagerUtil() {}

    public static DynamicRegistryManager.Immutable getRegistryManager() {
        return registryManager;
    }

    public static void setRegistryManager(DynamicRegistryManager.Immutable registryManager) {
        LootManagerUtil.registryManager = registryManager;
    }
}
