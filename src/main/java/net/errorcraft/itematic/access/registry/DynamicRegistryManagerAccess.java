package net.errorcraft.itematic.access.registry;

import net.minecraft.registry.DynamicRegistryManager;

public interface DynamicRegistryManagerAccess {
    default DynamicRegistryManager getRegistryManager() {
        return null;
    }
    default void setRegistryManager(DynamicRegistryManager registryManager) {}
}
