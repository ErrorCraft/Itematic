package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class ShulkerBoxBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 2, 1);

    @GameTest(templateName = "itematic:block.shulker_box")
    public void breakingShulkerBoxInCreativeModeDropsShulkerBox(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.CREATIVE);
        ServerWorld world = context.getWorld();
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        state.getBlock().onBreak(world, absolutePos, state, player);
        context.addInstantFinalTask(() -> context.expectItem(context.getWorld().itematic$getItem(ItemKeys.SHULKER_BOX).value()));
    }

    @GameTest(templateName = "itematic:block.red_shulker_box")
    public void breakingRedShulkerBoxInCreativeModeDropsRedShulkerBox(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.CREATIVE);
        ServerWorld world = context.getWorld();
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        state.getBlock().onBreak(world, absolutePos, state, player);
        context.addInstantFinalTask(() -> context.expectItem(context.getWorld().itematic$getItem(ItemKeys.RED_SHULKER_BOX).value()));
    }
}
