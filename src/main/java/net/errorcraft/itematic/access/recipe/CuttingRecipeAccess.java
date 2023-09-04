package net.errorcraft.itematic.access.recipe;

import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;

public interface CuttingRecipeAccess {
    default void setResult(RegistryEntry<Item> result, int count) {}
}
