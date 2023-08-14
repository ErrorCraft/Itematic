package net.errorcraft.itematic.access.server;

import net.minecraft.registry.DynamicRegistryManager;

public interface ServerAdvancementLoaderAccess {
    default void setRegistryManager(DynamicRegistryManager registryManager) {}
}
