package net.errorcraft.itematic.server;

import net.minecraft.registry.DynamicRegistryManager;

public class ServerAdvancementLoaderUtil {
    private static DynamicRegistryManager registryManager;

    public static DynamicRegistryManager getRegistryManager() {
        return registryManager;
    }

    public static void setRegistryManager(DynamicRegistryManager registryManager) {
        ServerAdvancementLoaderUtil.registryManager = registryManager;
    }
}
