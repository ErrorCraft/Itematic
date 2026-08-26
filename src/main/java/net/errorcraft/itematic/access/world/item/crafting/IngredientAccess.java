package net.errorcraft.itematic.access.world.item.crafting;

import net.minecraft.world.item.ItemStackTemplate;

import java.util.Optional;

public interface IngredientAccess {
    default Optional<ItemStackTemplate> itematic$remainder() {
        return Optional.empty();
    }
    default void itematic$setRemainder(Optional<ItemStackTemplate> remainder) {}
}
