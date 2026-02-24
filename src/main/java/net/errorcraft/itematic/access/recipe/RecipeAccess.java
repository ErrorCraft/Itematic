package net.errorcraft.itematic.access.recipe;

import net.minecraft.item.Item;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.registry.RegistryEntryLookup;

import java.util.List;

public interface RecipeAccess {
    default IngredientPlacement itematic$ingredientPlacement(RegistryEntryLookup<Item> items) {
        return null;
    }
    default List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        return null;
    }
}
