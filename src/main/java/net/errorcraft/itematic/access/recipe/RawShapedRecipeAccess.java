package net.errorcraft.itematic.access.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;

public interface RawShapedRecipeAccess {
    NonNullList<ItemStack> itematic$remainder(CraftingInput input);
}
