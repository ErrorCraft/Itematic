package net.errorcraft.itematic.access.recipe;

import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface IngredientAccess {
    default Optional<ItemStack> itematic$remainder() {
        return Optional.empty();
    }
    default void itematic$setRemainder(Optional<ItemStack> remainder) {}
}
