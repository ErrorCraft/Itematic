package net.errorcraft.itematic.recipe;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public record BrewingRecipe<T>(T input, RegistryKey<Item> ingredient, T output) {
}
