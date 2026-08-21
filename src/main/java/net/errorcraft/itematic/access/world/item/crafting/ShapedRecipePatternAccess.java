package net.errorcraft.itematic.access.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;

public interface ShapedRecipePatternAccess {
    default NonNullList<ItemStack> itematic$remainder(CraftingInput input) {
        return NonNullList.create();
    }
}
