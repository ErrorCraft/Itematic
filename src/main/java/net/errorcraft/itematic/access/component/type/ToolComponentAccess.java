package net.errorcraft.itematic.access.component.type;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public interface ToolComponentAccess {
    default float itematic$getSpeed(ItemStack stack, BlockState state) {
        return 0.0f;
    }

    default boolean itematic$isCorrectForDrops(ItemStack stack, BlockState state) {
        return false;
    }

    interface RuleAccess {
        default boolean itematic$matches(ItemStack stack, BlockState state) {
            return false;
        }
    }
}
