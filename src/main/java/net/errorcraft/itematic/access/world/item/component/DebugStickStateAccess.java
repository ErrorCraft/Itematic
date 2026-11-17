package net.errorcraft.itematic.access.world.item.component;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface DebugStickStateAccess {
    default void itematic$cycleProperty(LivingEntity user, BlockState state, LevelAccessor level, BlockPos pos, ItemStack stack) {}
    default InteractionResult itematic$cycleSelectedPropertyValue(UseOnContext context) {
        throw new AssertionError("Implemented via mixin");
    }
}
