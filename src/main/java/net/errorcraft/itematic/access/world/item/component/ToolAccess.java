package net.errorcraft.itematic.access.world.item.component;

import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface ToolAccess {
    default float itematic$getSpeed(ItemStack stack, BlockState state) {
        return 0.0f;
    }
    default boolean itematic$isCorrectForDrops(ItemStack stack, BlockState state) {
        return false;
    }

    interface RuleAccess {
        default Optional<ItemPredicate> itematic$item() {
            return Optional.empty();
        }
        default void itematic$setItem(Optional<ItemPredicate> item) {}
        default boolean itematic$matches(ItemStack stack, BlockState state) {
            return false;
        }
    }
}
