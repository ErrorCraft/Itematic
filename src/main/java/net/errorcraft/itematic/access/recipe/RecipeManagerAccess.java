package net.errorcraft.itematic.access.recipe;

import net.minecraft.item.Item;
import net.minecraft.registry.Registry;

public interface RecipeManagerAccess {
    default void setItemRegistry(Registry<Item> registry) {}
}
