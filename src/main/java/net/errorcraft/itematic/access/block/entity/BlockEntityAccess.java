package net.errorcraft.itematic.access.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface BlockEntityAccess {
    default boolean itematic$placedFromItemStack(World world, @Nullable PlayerEntity player, BlockState state, BlockPos pos, ItemStack stack) {
        return BlockItem.writeNbtToBlockEntity(world, player, pos, stack);
    }
}
