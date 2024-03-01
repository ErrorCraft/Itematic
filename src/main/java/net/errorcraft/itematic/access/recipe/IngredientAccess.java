package net.errorcraft.itematic.access.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;

public interface IngredientAccess {
    default ItemStack[] itematic$getMatchingStacks(RegistryWrapper.WrapperLookup lookup) {
        return new ItemStack[0];
    }
}
