package net.errorcraft.itematic.gametest.block;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.references.BlockItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;

public class BeehiveBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.beehive")
    public void breakingBeehiveWithHoneyInCreativeModeDropsBeehive(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.CREATIVE);
        ServerLevel level = helper.getLevel();
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        state.getBlock().playerWillDestroy(level, absolutePos, state, player);
        helper.succeedIf(() -> helper.assertItemEntityPresent(helper.getLevel().itematic$getItem(BlockItemIds.BEEHIVE.item()).value()));
    }

    @GameTest(structure = "itematic:block.bee_nest")
    public void breakingBeeNestWithHoneyInCreativeModeDropsBeeNest(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.CREATIVE);
        ServerLevel level = helper.getLevel();
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        state.getBlock().playerWillDestroy(level, absolutePos, state, player);
        helper.succeedIf(() -> helper.assertItemEntityPresent(helper.getLevel().itematic$getItem(BlockItemIds.BEE_NEST.item()).value()));
    }
}
