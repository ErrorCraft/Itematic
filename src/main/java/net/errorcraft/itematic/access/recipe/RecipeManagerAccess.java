package net.errorcraft.itematic.access.recipe;

import net.minecraft.registry.DynamicRegistryManager;

public interface RecipeManagerAccess {
    default void itematic$setRegistryManager(DynamicRegistryManager registryManager) {}
}
