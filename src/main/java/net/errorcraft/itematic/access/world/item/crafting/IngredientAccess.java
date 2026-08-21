package net.errorcraft.itematic.access.world.item.crafting;

import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface IngredientAccess {
    default Optional<ItemStack> itematic$remainder() {
        return Optional.empty();
    }
    default void itematic$setRemainder(Optional<ItemStack> remainder) {}
}
