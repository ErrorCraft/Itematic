package net.errorcraft.itematic.access.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;

import java.util.Optional;

public interface IngredientAccess {
    default ItemStack[] itematic$getMatchingStacks(RegistryWrapper.WrapperLookup lookup) {
        return new ItemStack[0];
    }
    default Optional<ItemStack> itematic$remainder() {
        return Optional.empty();
    }
    default void itematic$setRemainder(Optional<ItemStack> remainder) {}
}
