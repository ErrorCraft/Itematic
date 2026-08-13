package net.errorcraft.itematic.gametest.item;

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

public class BrushTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 2, 1);
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 0);

    @GameTest(structure = "itematic:item.brush.platform")
    public void usingBrushDoesNotStartBrushing(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack brush = world.itematic$createStack(ItemIds.BRUSH);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, brush);
        brush.use(world, player, InteractionHand.MAIN_HAND);
        context.succeedIf(() -> Assert.isFalse(
            context,
            player.isUsingItem(),
            () -> "Expected Player not to have started using a Brush"
        ));
    }

    @GameTest(structure = "itematic:item.brush.platform.suspicious_sand")
    public void usingBrushOnBlockStartsBrushing(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack brush = world.itematic$createStack(ItemIds.BRUSH);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, brush);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.UP);
        context.succeedIf(() -> Assert.isTrue(
            context,
            player.isUsingItem(),
            () -> "Expected Player to have started using a Brush"
        ));
    }

    @GameTest(structure = "itematic:item.brush.platform.short_grass")
    public void usingBrushOnIntangibleBlockDoesNotStartBrushing(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack brush = world.itematic$createStack(ItemIds.BRUSH);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, brush);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.UP);
        context.succeedOnTickWhen(1, () -> Assert.isFalse(
            context,
            player.isUsingItem(),
            () -> "Expected Player not to have started using a Brush"
        ));
    }
}
