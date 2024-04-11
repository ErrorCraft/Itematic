package net.errorcraft.itematic.access.recipe;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public interface BrewingRecipeRegistryBuilderAccess {
    default void itematic$registerItemRecipe(RegistryKey<Item> from, RegistryKey<Item> ingredient, RegistryKey<Item> to) {}
    default void itematic$registerWaterPotionRecipe(RegistryKey<Item> key, RegistryEntry<Potion> output) {}
    default void itematic$registerAwkwardPotionRecipe(RegistryKey<Item> key, RegistryEntry<Potion> output) {}
    default void itematic$registerLongPotionRecipe(RegistryEntry<Potion> input, RegistryEntry<Potion> output) {}
    default void itematic$registerStrongPotionRecipe(RegistryEntry<Potion> input, RegistryEntry<Potion> output) {}
    default void itematic$registerNegatingPotionRecipe(RegistryEntry<Potion> input, RegistryEntry<Potion> output) {}
}
