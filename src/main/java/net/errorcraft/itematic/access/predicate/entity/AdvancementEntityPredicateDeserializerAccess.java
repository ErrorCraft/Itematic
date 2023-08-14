package net.errorcraft.itematic.access.predicate.entity;

import net.minecraft.registry.DynamicRegistryManager;

public interface AdvancementEntityPredicateDeserializerAccess {
    default void setRegistryManager(DynamicRegistryManager registryManager) {}
}
