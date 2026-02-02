package net.errorcraft.itematic.access.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.util.collection.DefaultedList;

public interface RawShapedRecipeAccess {
    DefaultedList<ItemStack> itematic$remainder(CraftingRecipeInput input);
}
