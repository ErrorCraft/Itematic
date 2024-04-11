package net.errorcraft.itematic.access.recipe;

import net.errorcraft.itematic.recipe.BrewingRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

import java.util.List;

public interface BrewingRecipeRegistryAccess {
    default void itematic$setItemRecipes(List<BrewingRecipe<RegistryKey<Item>>> itemRecipes) {}
    default void itematic$setPotionRecipes(List<BrewingRecipe<RegistryEntry<Potion>>> potionRecipes) {}
    default ItemStack itematic$craft(ItemStack ingredient, ItemStack input, World world) {
        return ItemStack.EMPTY;
    }
}
