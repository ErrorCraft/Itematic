package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class UseableOnFluidItemComponentTestSuite {
    private static final BlockPos SPAWN_POSITION_ON_LAND = new BlockPos(2, 6, 0);
    private static final BlockPos SPAWN_POSITION_IN_WATER = new BlockPos(2, 2, 2);
    private static final BlockPos LOOK_AT_WATER_POSITION_ON_LAND = SPAWN_POSITION_ON_LAND.add(0, -1, 1);
    private static final BlockPos ABOVE_LOOK_AT_WATER_POSITION_ON_LAND = LOOK_AT_WATER_POSITION_ON_LAND.add(0, 1, 0);
    private static final BlockPos LOOK_AT_AIR_POSITION_ON_LAND = SPAWN_POSITION_ON_LAND.add(0, 1, 4);
    private static final BlockPos LOOK_AT_WATER_POSITION_IN_WATER = SPAWN_POSITION_IN_WATER.add(0, -1, 1);
    private static final BlockPos ABOVE_LOOK_AT_WATER_POSITION_IN_WATER = LOOK_AT_WATER_POSITION_IN_WATER.add(0, 1, 0);
    private static final BlockPos LOOK_AT_AIR_POSITION_IN_WATER = SPAWN_POSITION_IN_WATER.add(0, 3, 0);

    @GameTest(templateName = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingLilyPadWhileLookingAtWaterPlacesLilyPad(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION_ON_LAND);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(LOOK_AT_WATER_POSITION_ON_LAND)));
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.LILY_PAD);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        TypedActionResult<ItemStack> result = stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> {
            context.assertTrue(result.getResult().isAccepted(), "Expected Lily Pad usage to be successful");
            context.expectBlock(Blocks.LILY_PAD, ABOVE_LOOK_AT_WATER_POSITION_ON_LAND);
        });
    }

    @GameTest(templateName = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingPigSpawnEggWhileLookingAtWaterSpawnsPigAtWater(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION_ON_LAND);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(LOOK_AT_WATER_POSITION_ON_LAND)));
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.PIG_SPAWN_EGG);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        TypedActionResult<ItemStack> result = stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> {
            context.assertTrue(result.getResult().isAccepted(), "Expected Pig Spawn Egg usage to be successful");
            context.expectEntityAt(EntityType.PIG, LOOK_AT_WATER_POSITION_ON_LAND);
        });
    }

    @GameTest(templateName = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingPigSpawnEggWhileLookingAtAirDoesNotSpawnPig(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION_ON_LAND);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(LOOK_AT_AIR_POSITION_ON_LAND)));
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.PIG_SPAWN_EGG);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        TypedActionResult<ItemStack> result = stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> {
            context.assertFalse(result.getResult().isAccepted(), "Expected Pig Spawn Egg usage to be unsuccessful");
            context.dontExpectEntity(EntityType.PIG);
        });
    }

    @GameTest(templateName = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingPigSpawnEggWhileLookingAtGroundUnderWaterSpawnsPigOnGroundUnderWater(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION_IN_WATER);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(LOOK_AT_WATER_POSITION_IN_WATER)));
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.PIG_SPAWN_EGG);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        TestUtil.useStackOnBlockInside(context, player, stack, ABOVE_LOOK_AT_WATER_POSITION_IN_WATER, Direction.DOWN);
        context.addInstantFinalTask(() -> context.expectEntityAt(EntityType.PIG, ABOVE_LOOK_AT_WATER_POSITION_IN_WATER));
    }

    @GameTest(templateName = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingPigSpawnEggWhileLookingAtWaterUnderWaterSpawnsPigAtPlayerEyes(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION_IN_WATER);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(LOOK_AT_AIR_POSITION_IN_WATER)));
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.PIG_SPAWN_EGG);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        TypedActionResult<ItemStack> result = stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> {
            context.assertTrue(result.getResult().isAccepted(), "Expected Pig Spawn Egg usage to be successful");
            BlockPos eyeBlockPos = SPAWN_POSITION_IN_WATER.add(0, (int) player.getStandingEyeHeight(), 0);
            context.expectEntityAt(EntityType.PIG, eyeBlockPos);
        });
    }
}
