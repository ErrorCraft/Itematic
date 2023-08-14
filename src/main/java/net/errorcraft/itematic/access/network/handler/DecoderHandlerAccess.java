package net.errorcraft.itematic.access.network.handler;

import net.minecraft.registry.DynamicRegistryManager;

public interface DecoderHandlerAccess {
    default void setRegistryManager(DynamicRegistryManager registryManager) {}
}
