package net.errorcraft.itematic.access.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface BlockEntityAccess {
    default boolean itematic$placedFromItemStack(Level world, @Nullable Player player, BlockState state, BlockPos pos, ItemStack stack) {
        return BlockItem.updateCustomBlockEntityTag(world, player, pos, stack);
    }
}
