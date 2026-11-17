package net.errorcraft.itematic.gametest.item.placement;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.references.ItemIds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class EntityPlacementTestSuite {
    private static final BlockPos FLOOR_POSITION = new BlockPos(1, 0, 1);
    private static final BlockPos WALL_POSITION = new BlockPos(1, 1, 2);
    private static final BlockPos CEILING_POSITION = new BlockPos(1, 2, 1);
    private static final BlockPos MIDDLE_BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.placement.entity.platform")
    public void usingEntityPlacerOnFloorPlacesEntityOnThatFloor(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.PIG_SPAWN_EGG)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.UP, FLOOR_POSITION);
            Assert.entityType(helper, EntityTypes.PIG)
                .existsAt(1.5d, 1.0d, 1.5d);
        });
    }

    @GameTest(structure = "itematic:item.placement.entity.platform.wall")
    public void usingEntityPlacerOnWallPlacesEntityNextToThatWall(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.PIG_SPAWN_EGG)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.NORTH, WALL_POSITION);
            Assert.entityType(helper, EntityTypes.PIG)
                .existsAt(1.5d, 1.0d, 1.5d);
        });
    }

    @GameTest(structure = "itematic:item.placement.entity.platform.ceiling")
    public void usingEntityPlacerOnCeilingPlacesEntityBelowThatCeiling(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.PIG_SPAWN_EGG)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.DOWN, CEILING_POSITION);
            Assert.entityType(helper, EntityTypes.PIG)
                .existsAt(1.5d, 1.0d, 1.5d);
        });
    }

    @GameTest(structure = "itematic:item.placement.entity.platform.stairs")
    public void usingEntityPlacerInsideBlockWithCollisionPlacesEntityAboveThatBlock(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.PIG_SPAWN_EGG)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.UP, MIDDLE_BLOCK_POSITION, 0.5d, 0.5d, 0.25d);
            Assert.entityType(helper, EntityTypes.PIG)
                .existsAt(1.5d, 2.0d, 1.5d);
        });
    }

    @GameTest(structure = "itematic:item.placement.entity.platform.torch")
    public void usingEntityPlacerInsideBlockWithoutCollisionPlacesEntityInsideThatBlock(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.PIG_SPAWN_EGG)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.UP, MIDDLE_BLOCK_POSITION, 0.5d, 0.625d, 0.5d);
            Assert.entityType(helper, EntityTypes.PIG)
                .existsAt(1.5d, 1.0d, 1.5d);
        });
    }

    @GameTest(structure = "itematic:item.placement.entity.platform.fence")
    public void usingEntityPlacerOnFloorOfBlockWithHigherVerticalCollisionPlacesEntityOnThatHigherFloor(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.PIG_SPAWN_EGG)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.UP, MIDDLE_BLOCK_POSITION, 0.5d, 1.0d, 0.5d);
            Assert.entityType(helper, EntityTypes.PIG)
                .existsAt(1.5d, 2.5d, 1.5d);
        });
    }

    @GameTest(structure = "itematic:item.placement.entity.platform.slab")
    public void usingEntityPlacerOnFloorOfBlockWithLowerVerticalCollisionPlacesEntityOnThatLowerFloor(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.PIG_SPAWN_EGG)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.UP, MIDDLE_BLOCK_POSITION, 0.5d, 0.5d, 0.5d);
            Assert.entityType(helper, EntityTypes.PIG)
                .existsAt(1.5d, 1.5d, 1.5d);
        });
    }
}
