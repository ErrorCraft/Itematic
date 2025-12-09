package net.errorcraft.itematic.access.component.type;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public interface ToolComponentAccess {
    default float itematic$getSpeed(ItemStack stack, BlockState state) {
        return 0.0f;
    }

    default boolean itematic$isCorrectForDrops(ItemStack stack, BlockState state) {
        return false;
    }
}
