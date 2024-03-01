package net.errorcraft.itematic.access.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;

import java.util.Collection;
import java.util.List;

public interface IngredientEntryAccess {
    default boolean itematic$test(ItemStack stack) {
        return false;
    }
    default Collection<ItemStack> itematic$getStacks(RegistryWrapper.WrapperLookup lookup) {
        return List.of();
    }
}
