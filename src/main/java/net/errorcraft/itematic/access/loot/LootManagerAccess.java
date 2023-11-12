package net.errorcraft.itematic.access.loot;

import net.minecraft.registry.DynamicRegistryManager;

public interface LootManagerAccess {
    default void itematic$setRegistryManager(DynamicRegistryManager.Immutable registryManager) {}
}
