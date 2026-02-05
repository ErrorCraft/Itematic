package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class BeehiveBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(templateName = "itematic:block.beehive")
    public void breakingBeehiveWithHoneyInCreativeModeDropsBeehive(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.CREATIVE);
        ServerWorld world = context.getWorld();
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        state.getBlock().onBreak(world, absolutePos, state, player);
        context.addInstantFinalTask(() -> context.expectItem(context.getWorld().itematic$getItem(ItemKeys.BEEHIVE).value()));
    }

    @GameTest(templateName = "itematic:block.bee_nest")
    public void breakingBeeNestWithHoneyInCreativeModeDropsBeeNest(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.CREATIVE);
        ServerWorld world = context.getWorld();
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        state.getBlock().onBreak(world, absolutePos, state, player);
        context.addInstantFinalTask(() -> context.expectItem(context.getWorld().itematic$getItem(ItemKeys.BEE_NEST).value()));
    }
}
