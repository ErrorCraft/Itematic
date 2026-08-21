package net.errorcraft.itematic.access.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface BlockEntityAccess {
    default boolean itematic$placedFromItemStack(Level level, @Nullable Player player, BlockState state, BlockPos pos, ItemStack stack) {
        return BlockItem.updateCustomBlockEntityTag(level, player, pos, stack);
    }
}
