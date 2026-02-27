package net.errorcraft.itematic.access.recipe;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.util.collection.DefaultedList;

public interface RecipeAccess {
    default ItemStack itematic$createIcon(RegistryEntryLookup<Item> items) {
        return new ItemStack(items.getOrThrow(ItemKeys.CRAFTING_TABLE));
    }
    default DefaultedList<Ingredient> itematic$ingredients(RegistryEntryLookup<Item> items) {
        return null;
    }
}
