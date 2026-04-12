package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class UseableOnFluidItemComponentTestSuite {
    private static final BlockPos SPAWN_POSITION_ON_LAND = new BlockPos(2, 5, 0);
    private static final BlockPos SPAWN_POSITION_IN_WATER = new BlockPos(2, 1, 2);
    private static final BlockPos LOOK_AT_WATER_POSITION_ON_LAND = SPAWN_POSITION_ON_LAND.add(0, -1, 1);
    private static final BlockPos ABOVE_LOOK_AT_WATER_POSITION_ON_LAND = LOOK_AT_WATER_POSITION_ON_LAND.add(0, 1, 0);
    private static final BlockPos LOOK_AT_AIR_POSITION_ON_LAND = SPAWN_POSITION_ON_LAND.add(0, 1, 4);
    private static final BlockPos LOOK_AT_WATER_POSITION_IN_WATER = SPAWN_POSITION_IN_WATER.add(0, -1, 1);
    private static final BlockPos ABOVE_LOOK_AT_WATER_POSITION_IN_WATER = LOOK_AT_WATER_POSITION_IN_WATER.add(0, 1, 0);
    private static final BlockPos LOOK_AT_AIR_POSITION_IN_WATER = SPAWN_POSITION_IN_WATER.add(0, 3, 0);

    @GameTest(structure = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingLilyPadWhileLookingAtWaterPlacesLilyPad(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION_ON_LAND);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(LOOK_AT_WATER_POSITION_ON_LAND)));
        ServerWorld world = context.getWorld();
        ItemStack lilyPad = world.itematic$createStack(ItemKeys.LILY_PAD);
        player.setStackInHand(Hand.MAIN_HAND, lilyPad);
        context.addFinalTask(() -> {
            ActionResult result = lilyPad.use(world, player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Lily Pad usage to be successful"
            );
            Assert.blockState(context, ABOVE_LOOK_AT_WATER_POSITION_ON_LAND)
                .is(Blocks.LILY_PAD);
        });
    }

    @GameTest(structure = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingPigSpawnEggWhileLookingAtWaterSpawnsPigAtWater(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION_ON_LAND);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(LOOK_AT_WATER_POSITION_ON_LAND)));
        ServerWorld world = context.getWorld();
        ItemStack pigSpawnEgg = world.itematic$createStack(ItemKeys.PIG_SPAWN_EGG);
        player.setStackInHand(Hand.MAIN_HAND, pigSpawnEgg);
        context.addFinalTask(() -> {
            ActionResult result = pigSpawnEgg.use(world, player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Pig Spawn Egg usage to be successful"
            );
            context.expectEntityAt(EntityType.PIG, LOOK_AT_WATER_POSITION_ON_LAND);
        });
    }

    @GameTest(structure = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingPigSpawnEggWhileLookingAtAirDoesNotSpawnPig(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION_ON_LAND);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(LOOK_AT_AIR_POSITION_ON_LAND)));
        ServerWorld world = context.getWorld();
        ItemStack pigSpawnEgg = world.itematic$createStack(ItemKeys.PIG_SPAWN_EGG);
        player.setStackInHand(Hand.MAIN_HAND, pigSpawnEgg);
        context.addFinalTask(() -> {
            ActionResult result = pigSpawnEgg.use(world, player, Hand.MAIN_HAND);
            Assert.isFalse(
                context,
                result.isAccepted(),
                () -> "Expected Pig Spawn Egg usage to be unsuccessful"
            );
            context.dontExpectEntity(EntityType.PIG);
        });
    }

    @GameTest(structure = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingPigSpawnEggWhileLookingAtGroundUnderWaterSpawnsPigOnGroundUnderWater(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION_IN_WATER);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(LOOK_AT_WATER_POSITION_IN_WATER)));
        ServerWorld world = context.getWorld();
        ItemStack pigSpawnEgg = world.itematic$createStack(ItemKeys.PIG_SPAWN_EGG);
        player.setStackInHand(Hand.MAIN_HAND, pigSpawnEgg);
        TestUtil.useStackOnBlockInside(context, player, pigSpawnEgg, ABOVE_LOOK_AT_WATER_POSITION_IN_WATER, Direction.DOWN);
        context.addFinalTask(() -> context.expectEntityAt(EntityType.PIG, ABOVE_LOOK_AT_WATER_POSITION_IN_WATER));
    }

    @GameTest(structure = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingPigSpawnEggWhileLookingAtWaterUnderWaterSpawnsPigAtPlayerEyes(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION_IN_WATER);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(LOOK_AT_AIR_POSITION_IN_WATER)));
        ServerWorld world = context.getWorld();
        ItemStack pigSpawnEgg = world.itematic$createStack(ItemKeys.PIG_SPAWN_EGG);
        player.setStackInHand(Hand.MAIN_HAND, pigSpawnEgg);
        ActionResult result = pigSpawnEgg.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> {
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Pig Spawn Egg usage to be successful"
            );
            BlockPos eyeBlockPos = SPAWN_POSITION_IN_WATER.add(0, (int) player.getStandingEyeHeight(), 0);
            context.expectEntityAt(EntityType.PIG, eyeBlockPos);
        });
    }
}
