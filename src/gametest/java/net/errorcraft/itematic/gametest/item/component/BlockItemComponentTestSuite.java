package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.Blocks;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.test.GameTest;
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

    @GameTest(templateName = "itematic:item.component.block.platform")
    public void usingStoneOnGroundPlacesStone(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.STONE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.STONE, PLACED_BLOCK_POSITION));
    }

    @GameTest(templateName = "itematic:item.component.block.platform")
    public void usingOakSlabOnGroundPlacesOakSlab(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OAK_SLAB);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.checkBlockState(PLACED_BLOCK_POSITION, state -> state.get(Properties.SLAB_TYPE) == SlabType.BOTTOM, () -> "Expected placed oak slab to be of bottom type"));
    }

    @GameTest(templateName = "itematic:item.component.block.oak_slab.lower")
    public void usingOakSlabOnLowerOakSlabPlacesDoubleOakSlab(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OAK_SLAB);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, PLACED_BLOCK_POSITION, Direction.DOWN);
        context.addInstantFinalTask(() -> context.checkBlockState(PLACED_BLOCK_POSITION, state -> state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE, () -> "Expected placed oak slab to be of double type"));
    }

    @GameTest(templateName = "itematic:item.component.block.oak_slab.upper")
    public void usingOakSlabOnUpperOakSlabPlacesDoubleOakSlab(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OAK_SLAB);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, PLACED_BLOCK_POSITION, Direction.DOWN);
        context.addInstantFinalTask(() -> context.checkBlockState(PLACED_BLOCK_POSITION, state -> state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE, () -> "Expected placed oak slab to be of double type"));
    }

    @GameTest(templateName = "itematic:item.component.block.platform.grass_block")
    public void usingTallGrassOnGroundPlacesTallGrass(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.TALL_GRASS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            context.checkBlockState(PLACED_BLOCK_POSITION, state -> state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER, () -> "Expected lower half of tall grass to be placed");
            context.checkBlockState(ABOVE_PLACED_BLOCK_POSITION, state -> state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER, () -> "Expected upper half of tall grass to be placed");
        });
    }

    @GameTest(templateName = "itematic:item.component.block.platform.grass_block.blocked_off_above")
    public void usingTallGrassOnGroundWhileBlockedOffDoesNotPlaceTallGrass(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.TALL_GRASS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.dontExpectBlock(Blocks.TALL_GRASS, PLACED_BLOCK_POSITION));
    }

    @GameTest(templateName = "itematic:item.component.block.platform")
    public void usingSkeletonSkullOnGroundPlacesSkeletonSkull(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SKELETON_SKULL);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.SKELETON_SKULL, PLACED_BLOCK_POSITION));
    }

    @GameTest(templateName = "itematic:item.component.block.platform.wall")
    public void usingSkeletonSkullOnWallPlacesSkeletonWallSkull(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SKELETON_SKULL);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, WALL_POSITION, Direction.NORTH);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.SKELETON_WALL_SKULL, PLACED_BLOCK_POSITION));
    }

    @GameTest(templateName = "itematic:item.component.block.platform.ceiling")
    public void usingOakHangingSignOnCeilingPlacesOakHangingSign(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OAK_HANGING_SIGN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, ABOVE_PLACED_BLOCK_POSITION, Direction.DOWN);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.OAK_HANGING_SIGN, PLACED_BLOCK_POSITION));
    }

    @GameTest(templateName = "itematic:item.component.block.platform.wall")
    public void usingOakHangingSignOnWallPlacesOakWallHangingSign(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OAK_HANGING_SIGN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, WALL_POSITION, Direction.NORTH);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.OAK_WALL_HANGING_SIGN, PLACED_BLOCK_POSITION));
    }

    @GameTest(templateName = "itematic:item.component.block.platform")
    public void usingScaffoldingOnGroundPlacesScaffolding(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.SCAFFOLDING, PLACED_BLOCK_POSITION));
    }

    @GameTest(templateName = "itematic:item.component.block.scaffolding.single_block")
    public void usingScaffoldingOnTopFaceOfScaffoldingPlacesScaffoldingHorizontally(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, PLACED_BLOCK_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.SCAFFOLDING, HORIZONTAL_SCAFFOLDING_OFFSET));
    }

    @GameTest(templateName = "itematic:item.component.block.scaffolding.single_block")
    public void usingScaffoldingOnTopFaceOfBlockBelowScaffoldingPlacesScaffoldingHorizontally(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        player.setYaw(0.0f);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.SCAFFOLDING, HORIZONTAL_SCAFFOLDING_OFFSET));
    }

    @GameTest(templateName = "itematic:item.component.block.scaffolding.single_block")
    public void usingScaffoldingOnSideFaceOfScaffoldingPlacesScaffoldingVertically(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, PLACED_BLOCK_POSITION, Direction.SOUTH);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.SCAFFOLDING, VERTICAL_SCAFFOLDING_OFFSET));
    }

    @GameTest(templateName = "itematic:item.component.block.scaffolding.horizontal.max_distance")
    public void usingScaffoldingForHorizontalPlacementFailsAfterReachingMaxDistance(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, PLACED_BLOCK_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.dontExpectBlock(Blocks.SCAFFOLDING, HORIZONTAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET));
    }

    @GameTest(templateName = "itematic:item.component.block.scaffolding.vertical.max_distance")
    public void usingScaffoldingForVerticalPlacementIgnoresMaxDistance(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, PLACED_BLOCK_POSITION, Direction.SOUTH);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.SCAFFOLDING, VERTICAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET));
    }

    @GameTest(templateName = "itematic:item.component.block.platform")
    public void usingCommandBlockInSurvivalModeOnGroundDoesNotPlaceCommandBlock(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.COMMAND_BLOCK);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.dontExpectBlock(Blocks.COMMAND_BLOCK, PLACED_BLOCK_POSITION));
    }
}
