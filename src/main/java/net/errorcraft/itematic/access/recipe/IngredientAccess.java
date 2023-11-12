package net.errorcraft.itematic.access.recipe;

import net.minecraft.item.Item;
import net.minecraft.registry.Registry;

public interface IngredientAccess {
    default void itematic$initMatchingStacks(Registry<Item> registry) {}
}
