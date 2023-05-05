package net.errorcraft.itematic.access.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;

import java.util.Collection;
import java.util.List;

public interface IngredientEntryAccess {
    default Collection<ItemStack> getStacks(Registry<Item> registry) {
        return List.of();
    }
}
