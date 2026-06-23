package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class PaintingTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);
    private static final BlockPos PAINTING_POSITION = BLOCK_POSITION.add(0, 0, -1);

    @GameTest(structure = "itematic:item.painting.platform")
    public void usingPaintingOnVerticalSidePlacesPainting(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.PAINTING));
        world.spawnEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.NORTH);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.PAINTING)
            .existsAt(PAINTING_POSITION));
    }

    @GameTest(structure = "itematic:item.painting.platform")
    public void usingPaintingOnTopSideDoesNotPlacePainting(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.PAINTING));
        world.spawnEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.PAINTING)
            .doesNotExist());
    }

    @GameTest(structure = "itematic:item.painting.platform")
    public void usingPaintingOnBottomSideDoesNotPlacePainting(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.PAINTING));
        world.spawnEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.DOWN);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.PAINTING)
            .doesNotExist());
    }
}
