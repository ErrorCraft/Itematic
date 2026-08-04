package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class PaintingTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);
    private static final BlockPos PAINTING_POSITION = BLOCK_POSITION.offset(0, 0, -1);

    @GameTest(structure = "itematic:item.painting.platform")
    public void usingPaintingOnVerticalSidePlacesPainting(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemKeys.PAINTING));
        world.addFreshEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.NORTH);
        context.succeedIf(() -> Assert.entityType(context, EntityType.PAINTING)
            .existsAt(PAINTING_POSITION));
    }

    @GameTest(structure = "itematic:item.painting.platform")
    public void usingPaintingOnTopSideDoesNotPlacePainting(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemKeys.PAINTING));
        world.addFreshEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.UP);
        context.succeedIf(() -> Assert.entityType(context, EntityType.PAINTING)
            .doesNotExist());
    }

    @GameTest(structure = "itematic:item.painting.platform")
    public void usingPaintingOnBottomSideDoesNotPlacePainting(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemKeys.PAINTING));
        world.addFreshEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.DOWN);
        context.succeedIf(() -> Assert.entityType(context, EntityType.PAINTING)
            .doesNotExist());
    }
}
