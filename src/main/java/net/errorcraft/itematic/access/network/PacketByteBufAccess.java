package net.errorcraft.itematic.access.network;

import net.minecraft.registry.DynamicRegistryManager;

public interface PacketByteBufAccess {
    default void setRegistryManager(DynamicRegistryManager registryManager) {}
}
