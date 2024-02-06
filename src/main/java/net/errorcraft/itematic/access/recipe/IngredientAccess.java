package net.errorcraft.itematic.access.recipe;

import net.minecraft.registry.RegistryWrapper;

public interface IngredientAccess {
    default void itematic$initMatchingStacks(RegistryWrapper.WrapperLookup lookup) {}
}
