package net.errorcraft.itematic.gametest.item.component;

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
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;

public class BlockItemComponentTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 0);
    private static final BlockPos PLACED_BLOCK_POSITION = GROUND_POSITION.offset(0, 1, 0);
    private static final BlockPos ABOVE_PLACED_BLOCK_POSITION = PLACED_BLOCK_POSITION.offset(0, 1, 0);
    private static final BlockPos WALL_POSITION = GROUND_POSITION.offset(0, 1, 1);
    private static final int BEYOND_MAX_SCAFFOLDING_DISTANCE = ScaffoldingBlock.STABILITY_MAX_DISTANCE + 1;
    private static final BlockPos HORIZONTAL_SCAFFOLDING_OFFSET = PLACED_BLOCK_POSITION.offset(0, 0, 1);
    private static final BlockPos VERTICAL_SCAFFOLDING_OFFSET = PLACED_BLOCK_POSITION.offset(0, 1, 0);
    private static final BlockPos HORIZONTAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET = PLACED_BLOCK_POSITION.offset(0, 0, BEYOND_MAX_SCAFFOLDING_DISTANCE);
    private static final BlockPos VERTICAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET = PLACED_BLOCK_POSITION.offset(0, BEYOND_MAX_SCAFFOLDING_DISTANCE, 0);

    @GameTest(structure = "itematic:item.component.block.platform")
    public void usingStoneOnGroundPlacesStone(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack stone = world.itematic$createStack(ItemIds.STONE);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, stone);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stone, GROUND_POSITION, Direction.UP);
        context.succeedIf(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .is(Blocks.STONE));
    }

    @GameTest(structure = "itematic:item.component.block.platform")
    public void usingOakSlabOnGroundPlacesOakSlab(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack oakSlab = world.itematic$createStack(ItemIds.OAK_SLAB);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oakSlab);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakSlab, GROUND_POSITION, Direction.UP);
        context.succeedIf(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .hasProperty(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM, () -> "Expected placed Oak Slab to be of bottom type"));
    }

    @GameTest(structure = "itematic:item.component.block.oak_slab.lower")
    public void usingOakSlabOnLowerOakSlabPlacesDoubleOakSlab(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack oakSlab = world.itematic$createStack(ItemIds.OAK_SLAB);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oakSlab);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakSlab, PLACED_BLOCK_POSITION, Direction.DOWN);
        context.succeedIf(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .hasProperty(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE, () -> "Expected placed Oak Slab to be of double type"));
    }

    @GameTest(structure = "itematic:item.component.block.oak_slab.upper")
    public void usingOakSlabOnUpperOakSlabPlacesDoubleOakSlab(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack oakSlab = world.itematic$createStack(ItemIds.OAK_SLAB);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oakSlab);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakSlab, PLACED_BLOCK_POSITION, Direction.DOWN);
        context.succeedIf(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .hasProperty(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE, () -> "Expected placed Oak Slab to be of double type"));
    }

    @GameTest(structure = "itematic:item.component.block.platform.grass_block")
    public void usingTallGrassOnGroundPlacesTallGrass(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack tallGrass = world.itematic$createStack(ItemIds.TALL_GRASS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, tallGrass);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, tallGrass, GROUND_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.blockState(context, PLACED_BLOCK_POSITION)
                .hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER, () -> "Expected lower half of Tall Grass to be placed");
            Assert.blockState(context, ABOVE_PLACED_BLOCK_POSITION)
                .hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER, () -> "Expected upper half of Tall Grass to be placed");
        });
    }

    @GameTest(structure = "itematic:item.component.block.platform.grass_block.blocked_off_above")
    public void usingTallGrassOnGroundWhileBlockedOffDoesNotPlaceTallGrass(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack tallGrass = world.itematic$createStack(ItemIds.TALL_GRASS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, tallGrass);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, tallGrass, GROUND_POSITION, Direction.UP);
        context.succeedIf(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .isNot(Blocks.TALL_GRASS));
    }

    @GameTest(structure = "itematic:item.component.block.platform")
    public void usingSkeletonSkullOnGroundPlacesSkeletonSkull(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setXRot(90.0f);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemIds.SKELETON_SKULL));
        world.addFreshEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.succeedIf(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .is(Blocks.SKELETON_SKULL));
    }

    @GameTest(structure = "itematic:item.component.block.platform.wall")
    public void usingSkeletonSkullOnWallPlacesSkeletonWallSkull(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack skeletonSkull = world.itematic$createStack(ItemIds.SKELETON_SKULL);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, skeletonSkull);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, skeletonSkull, WALL_POSITION, Direction.NORTH);
        context.succeedIf(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .is(Blocks.SKELETON_WALL_SKULL));
    }

    @GameTest(structure = "itematic:item.component.block.platform.ceiling")
    public void usingOakHangingSignOnCeilingPlacesOakHangingSign(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setXRot(-90.0f);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemIds.OAK_HANGING_SIGN));
        world.addFreshEntity(player);
        TestUtil.useBlock(context, ABOVE_PLACED_BLOCK_POSITION, player, Direction.DOWN);
        context.succeedIf(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .is(Blocks.OAK_HANGING_SIGN));
    }

    @GameTest(structure = "itematic:item.component.block.platform.wall")
    public void usingOakHangingSignOnWallPlacesOakWallHangingSign(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack oakHangingSign = world.itematic$createStack(ItemIds.OAK_HANGING_SIGN);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oakHangingSign);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakHangingSign, WALL_POSITION, Direction.NORTH);
        context.succeedIf(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .is(Blocks.OAK_WALL_HANGING_SIGN));
    }

    @GameTest(structure = "itematic:item.component.block.platform")
    public void usingScaffoldingOnGroundPlacesScaffolding(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack scaffolding = world.itematic$createStack(ItemIds.SCAFFOLDING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, scaffolding);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, scaffolding, GROUND_POSITION, Direction.UP);
        context.succeedIf(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .is(Blocks.SCAFFOLDING));
    }

    @GameTest(structure = "itematic:item.component.block.scaffolding.single_block")
    public void usingScaffoldingOnTopFaceOfScaffoldingPlacesScaffoldingHorizontally(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack scaffolding = world.itematic$createStack(ItemIds.SCAFFOLDING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, scaffolding);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, scaffolding, PLACED_BLOCK_POSITION, Direction.UP);
        context.succeedIf(() -> Assert.blockState(context, HORIZONTAL_SCAFFOLDING_OFFSET)
            .is(Blocks.SCAFFOLDING));
    }

    @GameTest(structure = "itematic:item.component.block.scaffolding.single_block")
    public void usingScaffoldingOnTopFaceOfBlockBelowScaffoldingPlacesScaffoldingHorizontally(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack scaffolding = world.itematic$createStack(ItemIds.SCAFFOLDING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, scaffolding);
        player.setYRot(0.0f);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, scaffolding, GROUND_POSITION, Direction.UP);
        context.succeedIf(() -> Assert.blockState(context, HORIZONTAL_SCAFFOLDING_OFFSET)
            .is(Blocks.SCAFFOLDING));
    }

    @GameTest(structure = "itematic:item.component.block.scaffolding.single_block")
    public void usingScaffoldingOnSideFaceOfScaffoldingPlacesScaffoldingVertically(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack scaffolding = world.itematic$createStack(ItemIds.SCAFFOLDING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, scaffolding);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, scaffolding, PLACED_BLOCK_POSITION, Direction.SOUTH);
        context.succeedIf(() -> Assert.blockState(context, VERTICAL_SCAFFOLDING_OFFSET)
            .is(Blocks.SCAFFOLDING));
    }

    @GameTest(structure = "itematic:item.component.block.scaffolding.horizontal.max_distance")
    public void usingScaffoldingForHorizontalPlacementFailsAfterReachingMaxDistance(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack scaffolding = world.itematic$createStack(ItemIds.SCAFFOLDING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, scaffolding);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, scaffolding, PLACED_BLOCK_POSITION, Direction.UP);
        context.succeedIf(() -> Assert.blockState(context, HORIZONTAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET)
            .isNot(Blocks.SCAFFOLDING));
    }

    @GameTest(structure = "itematic:item.component.block.scaffolding.vertical.max_distance")
    public void usingScaffoldingForVerticalPlacementIgnoresMaxDistance(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack scaffolding = world.itematic$createStack(ItemIds.SCAFFOLDING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, scaffolding);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, scaffolding, PLACED_BLOCK_POSITION, Direction.SOUTH);
        context.succeedIf(() -> Assert.blockState(context, VERTICAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET)
            .is(Blocks.SCAFFOLDING));
    }

    @GameTest(structure = "itematic:item.component.block.platform")
    public void usingCommandBlockInSurvivalModeOnGroundDoesNotPlaceCommandBlock(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack commandBlock = world.itematic$createStack(ItemIds.COMMAND_BLOCK);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, commandBlock);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, commandBlock, GROUND_POSITION, Direction.UP);
        context.succeedIf(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .isNot(Blocks.COMMAND_BLOCK));
    }
}
