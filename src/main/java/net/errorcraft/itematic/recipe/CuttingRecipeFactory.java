package net.errorcraft.itematic.recipe;

import net.minecraft.item.Item;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;

@FunctionalInterface
public interface CuttingRecipeFactory<T extends CuttingRecipe> {
    T create(String group, Ingredient ingredient, RegistryEntry<Item> result, int count);
}
