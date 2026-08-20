package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;

public class CandleBlockTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 1);
    private static final BlockPos PLACED_BLOCK_POSITION = GROUND_POSITION.offset(0, 1, 0);

    @GameTest(structure = "itematic:block.white_candle")
    public void usingSameCandleOnCandleBlockIncreasesCandles(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack whiteCandle = level.itematic$createStack(ItemIds.WHITE_CANDLE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, whiteCandle);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, whiteCandle, GROUND_POSITION, Direction.UP);
        helper.succeedIf(() -> Assert.blockState(helper, PLACED_BLOCK_POSITION)
            .hasProperty(CandleBlock.CANDLES, 2));
    }

    @GameTest(structure = "itematic:block.white_candle")
    public void usingDifferentlyColoredCandleOnCandleBlockDoesNotReplaceBlock(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack whiteCandle = level.itematic$createStack(ItemIds.WHITE_CANDLE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, whiteCandle);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, whiteCandle, GROUND_POSITION, Direction.UP);
        helper.succeedIf(() -> Assert.blockState(helper, PLACED_BLOCK_POSITION)
            .is(Blocks.WHITE_CANDLE));
    }
}
