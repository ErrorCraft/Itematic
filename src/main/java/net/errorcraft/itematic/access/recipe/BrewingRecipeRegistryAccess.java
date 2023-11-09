package net.errorcraft.itematic.access.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface BrewingRecipeRegistryAccess {
    default ItemStack itematic$craft(ItemStack ingredient, ItemStack input, World world) {
        return ItemStack.EMPTY;
    }
}
