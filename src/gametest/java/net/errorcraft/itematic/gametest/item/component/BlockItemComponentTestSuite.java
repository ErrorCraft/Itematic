package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.Blocks;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class BlockItemComponentTestSuite {
    private static final int BEYOND_MAX_DISTANCE = ScaffoldingBlock.MAX_DISTANCE + 1;
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 1, 0);
    private static final BlockPos SCAFFOLDING_POSITION = GROUND_POSITION.add(0, 1, 0);
    private static final BlockPos HORIZONTAL_SCAFFOLDING_OFFSET = SCAFFOLDING_POSITION.add(0, 0, 1);
    private static final BlockPos VERTICAL_SCAFFOLDING_OFFSET = SCAFFOLDING_POSITION.add(0, 1, 0);
    private static final BlockPos HORIZONTAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET = SCAFFOLDING_POSITION.add(0, 0, BEYOND_MAX_DISTANCE);
    private static final BlockPos VERTICAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET = SCAFFOLDING_POSITION.add(0, BEYOND_MAX_DISTANCE, 0);

    @GameTest(templateName = "itematic:item.component.block.scaffolding.empty")
    public void usingScaffoldingOnGroundPlacesScaffolding(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.SCAFFOLDING, SCAFFOLDING_POSITION));
    }

    @GameTest(templateName = "itematic:item.component.block.scaffolding.single_block")
    public void usingScaffoldingOnTopFaceOfScaffoldingPlacesScaffoldingHorizontally(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, SCAFFOLDING_POSITION, Direction.UP);
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
        TestUtil.useStackOnBlockInside(context, player, stack, SCAFFOLDING_POSITION, Direction.SOUTH);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.SCAFFOLDING, VERTICAL_SCAFFOLDING_OFFSET));
    }

    @GameTest(templateName = "itematic:item.component.block.scaffolding.horizontal.max_distance")
    public void usingScaffoldingForHorizontalPlacementFailsAfterReachingMaxDistance(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, SCAFFOLDING_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.dontExpectBlock(Blocks.SCAFFOLDING, HORIZONTAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET));
    }

    @GameTest(templateName = "itematic:item.component.block.scaffolding.vertical.max_distance")
    public void usingScaffoldingForVerticalPlacementIgnoresMaxDistance(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SCAFFOLDING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, SCAFFOLDING_POSITION, Direction.SOUTH);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.SCAFFOLDING, VERTICAL_SCAFFOLDING_BEYOND_MAX_DISTANCE_OFFSET));
    }
}
