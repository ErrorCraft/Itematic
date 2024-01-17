package net.errorcraft.itematic.access.client.recipebook;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;

import java.util.List;

public interface RecipeBookGroupAccess {
    default List<ItemStack> itematic$icons(Registry<Item> registry) {
        return List.of();
    }
}
