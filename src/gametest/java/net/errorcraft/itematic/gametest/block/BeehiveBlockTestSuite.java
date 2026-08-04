package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;

public class BeehiveBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.beehive")
    public void breakingBeehiveWithHoneyInCreativeModeDropsBeehive(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.CREATIVE);
        ServerLevel world = context.getLevel();
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        state.getBlock().playerWillDestroy(world, absolutePos, state, player);
        context.succeedIf(() -> context.assertItemEntityPresent(context.getLevel().itematic$getItem(ItemKeys.BEEHIVE).value()));
    }

    @GameTest(structure = "itematic:block.bee_nest")
    public void breakingBeeNestWithHoneyInCreativeModeDropsBeeNest(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.CREATIVE);
        ServerLevel world = context.getLevel();
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        state.getBlock().playerWillDestroy(world, absolutePos, state, player);
        context.succeedIf(() -> context.assertItemEntityPresent(context.getLevel().itematic$getItem(ItemKeys.BEE_NEST).value()));
    }
}
