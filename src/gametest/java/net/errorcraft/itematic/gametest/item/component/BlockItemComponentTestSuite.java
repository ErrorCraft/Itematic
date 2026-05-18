package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.block.Blocks;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class BlockItemComponentTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 0);
    private static final BlockPos PLACED_BLOCK_POSITION = GROUND_POSITION.add(0, 1, 0);
    private static final BlockPos ABOVE_PLACED_BLOCK_POSITION = PLACED_BLOCK_POSITION.add(0, 1, 0);
    private static final BlockPos WALL_POSITION = GROUND_POSITION.add(0, 1, 1);
    private static final int BEYOND_MAX_SCAFFOLDING_DISTANCE = ScaffoldingBlock.MAX_DISTANCE + 1;
    private static final BlockPos HORIZONTAL_SCAFFOLDING_OFFSET = PLACED_BLOCK_POSITION.add(0, 0, 1);
    private static final BlockPos VERTICAL_SCAFFOLDING_OFFSET = PLACED_BLOCK_POSITION.add(0, 1, 0);
    private static final BlockPos HORIZONTAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET = PLACED_BLOCK_POSITION.add(0, 0, BEYOND_MAX_SCAFFOLDING_DISTANCE);
    private static final BlockPos VERTICAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET = PLACED_BLOCK_POSITION.add(0, BEYOND_MAX_SCAFFOLDING_DISTANCE, 0);

    @GameTest(structure = "itematic:item.component.block.platform")
    public void usingStoneOnGroundPlacesStone(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stone = world.itematic$createStack(ItemKeys.STONE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stone);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stone, GROUND_POSITION, Direction.UP);
        context.addFinalTask(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .is(Blocks.STONE));
    }

    @GameTest(structure = "itematic:item.component.block.platform")
    public void usingOakSlabOnGroundPlacesOakSlab(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack oakSlab = world.itematic$createStack(ItemKeys.OAK_SLAB);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, oakSlab);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakSlab, GROUND_POSITION, Direction.UP);
        context.addFinalTask(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .hasProperty(Properties.SLAB_TYPE, SlabType.BOTTOM, () -> "Expected placed Oak Slab to be of bottom type"));
    }

    @GameTest(structure = "itematic:item.component.block.oak_slab.lower")
    public void usingOakSlabOnLowerOakSlabPlacesDoubleOakSlab(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack oakSlab = world.itematic$createStack(ItemKeys.OAK_SLAB);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, oakSlab);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakSlab, PLACED_BLOCK_POSITION, Direction.DOWN);
        context.addFinalTask(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .hasProperty(Properties.SLAB_TYPE, SlabType.DOUBLE, () -> "Expected placed Oak Slab to be of double type"));
    }

    @GameTest(structure = "itematic:item.component.block.oak_slab.upper")
    public void usingOakSlabOnUpperOakSlabPlacesDoubleOakSlab(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack oakSlab = world.itematic$createStack(ItemKeys.OAK_SLAB);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, oakSlab);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakSlab, PLACED_BLOCK_POSITION, Direction.DOWN);
        context.addFinalTask(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .hasProperty(Properties.SLAB_TYPE, SlabType.DOUBLE, () -> "Expected placed Oak Slab to be of double type"));
    }

    @GameTest(structure = "itematic:item.component.block.platform.grass_block")
    public void usingTallGrassOnGroundPlacesTallGrass(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack tallGrass = world.itematic$createStack(ItemKeys.TALL_GRASS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, tallGrass);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, tallGrass, GROUND_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.blockState(context, PLACED_BLOCK_POSITION)
                .hasProperty(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER, () -> "Expected lower half of Tall Grass to be placed");
            Assert.blockState(context, ABOVE_PLACED_BLOCK_POSITION)
                .hasProperty(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER, () -> "Expected upper half of Tall Grass to be placed");
        });
    }

    @GameTest(structure = "itematic:item.component.block.platform.grass_block.blocked_off_above")
    public void usingTallGrassOnGroundWhileBlockedOffDoesNotPlaceTallGrass(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack tallGrass = world.itematic$createStack(ItemKeys.TALL_GRASS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, tallGrass);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, tallGrass, GROUND_POSITION, Direction.UP);
        context.addFinalTask(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .isNot(Blocks.TALL_GRASS));
    }

    @GameTest(structure = "itematic:item.component.block.platform")
    public void usingSkeletonSkullOnGroundPlacesSkeletonSkull(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setPitch(90.0f);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.SKELETON_SKULL));
        world.spawnEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .is(Blocks.SKELETON_SKULL));
    }

    @GameTest(structure = "itematic:item.component.block.platform.wall")
    public void usingSkeletonSkullOnWallPlacesSkeletonWallSkull(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack skeletonSkull = world.itematic$createStack(ItemKeys.SKELETON_SKULL);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, skeletonSkull);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, skeletonSkull, WALL_POSITION, Direction.NORTH);
        context.addFinalTask(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .is(Blocks.SKELETON_WALL_SKULL));
    }

    @GameTest(structure = "itematic:item.component.block.platform.ceiling")
    public void usingOakHangingSignOnCeilingPlacesOakHangingSign(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setPitch(-90.0f);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.OAK_HANGING_SIGN));
        world.spawnEntity(player);
        TestUtil.useBlock(context, ABOVE_PLACED_BLOCK_POSITION, player, Direction.DOWN);
        context.addFinalTask(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .is(Blocks.OAK_HANGING_SIGN));
    }

    @GameTest(structure = "itematic:item.component.block.platform.wall")
    public void usingOakHangingSignOnWallPlacesOakWallHangingSign(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack oakHangingSign = world.itematic$createStack(ItemKeys.OAK_HANGING_SIGN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, oakHangingSign);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakHangingSign, WALL_POSITION, Direction.NORTH);
        context.addFinalTask(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .is(Blocks.OAK_WALL_HANGING_SIGN));
    }

    @GameTest(structure = "itematic:item.component.block.platform")
    public void usingScaffoldingOnGroundPlacesScaffolding(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack scaffolding = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, scaffolding);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, scaffolding, GROUND_POSITION, Direction.UP);
        context.addFinalTask(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .is(Blocks.SCAFFOLDING));
    }

    @GameTest(structure = "itematic:item.component.block.scaffolding.single_block")
    public void usingScaffoldingOnTopFaceOfScaffoldingPlacesScaffoldingHorizontally(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack scaffolding = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, scaffolding);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, scaffolding, PLACED_BLOCK_POSITION, Direction.UP);
        context.addFinalTask(() -> Assert.blockState(context, HORIZONTAL_SCAFFOLDING_OFFSET)
            .is(Blocks.SCAFFOLDING));
    }

    @GameTest(structure = "itematic:item.component.block.scaffolding.single_block")
    public void usingScaffoldingOnTopFaceOfBlockBelowScaffoldingPlacesScaffoldingHorizontally(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack scaffolding = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, scaffolding);
        player.setYaw(0.0f);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, scaffolding, GROUND_POSITION, Direction.UP);
        context.addFinalTask(() -> Assert.blockState(context, HORIZONTAL_SCAFFOLDING_OFFSET)
            .is(Blocks.SCAFFOLDING));
    }

    @GameTest(structure = "itematic:item.component.block.scaffolding.single_block")
    public void usingScaffoldingOnSideFaceOfScaffoldingPlacesScaffoldingVertically(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack scaffolding = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, scaffolding);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, scaffolding, PLACED_BLOCK_POSITION, Direction.SOUTH);
        context.addFinalTask(() -> Assert.blockState(context, VERTICAL_SCAFFOLDING_OFFSET)
            .is(Blocks.SCAFFOLDING));
    }

    @GameTest(structure = "itematic:item.component.block.scaffolding.horizontal.max_distance")
    public void usingScaffoldingForHorizontalPlacementFailsAfterReachingMaxDistance(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack scaffolding = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, scaffolding);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, scaffolding, PLACED_BLOCK_POSITION, Direction.UP);
        context.addFinalTask(() -> Assert.blockState(context, HORIZONTAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET)
            .isNot(Blocks.SCAFFOLDING));
    }

    @GameTest(structure = "itematic:item.component.block.scaffolding.vertical.max_distance")
    public void usingScaffoldingForVerticalPlacementIgnoresMaxDistance(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack scaffolding = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, scaffolding);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, scaffolding, PLACED_BLOCK_POSITION, Direction.SOUTH);
        context.addFinalTask(() -> Assert.blockState(context, VERTICAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET)
            .is(Blocks.SCAFFOLDING));
    }

    @GameTest(structure = "itematic:item.component.block.platform")
    public void usingCommandBlockInSurvivalModeOnGroundDoesNotPlaceCommandBlock(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack commandBlock = world.itematic$createStack(ItemKeys.COMMAND_BLOCK);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, commandBlock);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, commandBlock, GROUND_POSITION, Direction.UP);
        context.addFinalTask(() -> Assert.blockState(context, PLACED_BLOCK_POSITION)
            .isNot(Blocks.COMMAND_BLOCK));
    }
}
