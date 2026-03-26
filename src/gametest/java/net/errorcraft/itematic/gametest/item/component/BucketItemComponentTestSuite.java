package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class BucketItemComponentTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 2, 0);
    private static final BlockPos FACE_POSITION = new BlockPos(1, 1, 1);
    private static final BlockPos PLACED_POSITION = FACE_POSITION;

    @GameTest(templateName = "itematic:item.component.bucket.platform.water")
    public void usingBucketOnWaterTakesWaterAndGivesWaterBucket(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BUCKET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(FACE_POSITION)));
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        ActionResult result = stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> {
            Assert.fluidIsOf(context, Fluids.EMPTY, PLACED_POSITION);
            if (!(result instanceof ActionResult.Success successResult)) {
                throw new GameTestException("Expected bucket usage to be successful");
            }

            Assert.itemStackIsOf(successResult.getNewHandStack(), ItemKeys.WATER_BUCKET);
        });
    }

    @GameTest(templateName = "itematic:item.component.bucket.platform.powder_snow")
    public void usingBucketOnPowderSnowTakesWaterAndGivesPowderSnowBucket(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BUCKET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(FACE_POSITION)));
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        ActionResult result = stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> {
            Assert.fluidIsOf(context, Fluids.EMPTY, PLACED_POSITION);
            if (!(result instanceof ActionResult.Success successResult)) {
                throw new GameTestException("Expected bucket usage to be successful");
            }

            Assert.itemStackIsOf(successResult.getNewHandStack(), ItemKeys.POWDER_SNOW_BUCKET);
        });
    }

    @GameTest(templateName = "itematic:item.component.bucket.platform")
    public void usingWaterBucketOnGroundPlacesWater(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WATER_BUCKET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(FACE_POSITION)));
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> Assert.fluidIsOf(context, Fluids.WATER, PLACED_POSITION));
    }

    @GameTest(templateName = "itematic:item.component.bucket.platform")
    public void usingPowderSnowBucketOnGroundPlacesPowderSnow(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.POWDER_SNOW_BUCKET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(FACE_POSITION)));
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> context.expectBlock(Blocks.POWDER_SNOW, PLACED_POSITION));
    }

    @GameTest(templateName = "itematic:item.component.bucket.platform")
    public void usingPufferfishBucketOnGroundPlacesWaterAndPufferfish(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.PUFFERFISH_BUCKET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(FACE_POSITION)));
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> {
            Assert.fluidIsOf(context, Fluids.WATER, PLACED_POSITION);
            context.expectEntityAt(EntityType.PUFFERFISH, PLACED_POSITION);
        });
    }
}
