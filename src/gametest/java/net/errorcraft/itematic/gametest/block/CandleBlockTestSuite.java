package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class CandleBlockTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 1);
    private static final BlockPos PLACED_BLOCK_POSITION = GROUND_POSITION.add(0, 1, 0);

    @GameTest(templateName = "itematic:block.white_candle")
    public void usingSameCandleOnCandleBlockIncreasesCandles(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WHITE_CANDLE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.expectBlockProperty(PLACED_BLOCK_POSITION, CandleBlock.CANDLES, 2));
    }

    @GameTest(templateName = "itematic:block.white_candle")
    public void usingDifferentlyColoredCandleOnCandleBlockDoesNotReplaceBlock(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WHITE_CANDLE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.WHITE_CANDLE, PLACED_BLOCK_POSITION));
    }
}
