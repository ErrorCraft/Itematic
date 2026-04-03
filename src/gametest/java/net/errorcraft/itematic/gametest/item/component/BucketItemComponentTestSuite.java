package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
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

    @GameTest(structure = "itematic:item.component.bucket.platform.water")
    public void usingBucketOnWaterTakesWaterAndGivesWaterBucket(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack bucket = world.itematic$createStack(ItemKeys.BUCKET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(FACE_POSITION)));
        player.setStackInHand(Hand.MAIN_HAND, bucket);
        world.spawnEntity(player);
        ActionResult result = bucket.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> {
            Assert.fluidState(context, PLACED_POSITION)
                .is(Fluids.EMPTY);
            Assert.isInstance(
                context,
                result,
                ActionResult.Success.class,
                () -> "Expected Bucket usage to be successful",
                success -> Assert.itemStack(context, success.getNewHandStack())
                    .is(ItemKeys.WATER_BUCKET)
            );
        });
    }

    @GameTest(structure = "itematic:item.component.bucket.platform.powder_snow")
    public void usingBucketOnPowderSnowTakesWaterAndGivesPowderSnowBucket(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack bucket = world.itematic$createStack(ItemKeys.BUCKET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(FACE_POSITION)));
        player.setStackInHand(Hand.MAIN_HAND, bucket);
        world.spawnEntity(player);
        ActionResult result = bucket.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> {
            Assert.fluidState(context, PLACED_POSITION)
                .is(Fluids.EMPTY);
            Assert.isInstance(
                context,
                result,
                ActionResult.Success.class,
                () -> "Expected Bucket usage to be successful",
                success -> Assert.itemStack(context, success.getNewHandStack())
                    .is(ItemKeys.POWDER_SNOW_BUCKET)
            );
        });
    }

    @GameTest(structure = "itematic:item.component.bucket.platform")
    public void usingWaterBucketOnGroundPlacesWater(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack waterBucket = world.itematic$createStack(ItemKeys.WATER_BUCKET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(FACE_POSITION)));
        player.setStackInHand(Hand.MAIN_HAND, waterBucket);
        world.spawnEntity(player);
        waterBucket.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> Assert.fluidState(context, PLACED_POSITION)
            .is(Fluids.WATER));
    }

    @GameTest(structure = "itematic:item.component.bucket.platform")
    public void usingPowderSnowBucketOnGroundPlacesPowderSnow(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack powderSnowBucket = world.itematic$createStack(ItemKeys.POWDER_SNOW_BUCKET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(FACE_POSITION)));
        player.setStackInHand(Hand.MAIN_HAND, powderSnowBucket);
        world.spawnEntity(player);
        powderSnowBucket.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> Assert.blockState(context, PLACED_POSITION)
            .is(Blocks.POWDER_SNOW));
    }

    @GameTest(structure = "itematic:item.component.bucket.platform")
    public void usingPufferfishBucketOnGroundPlacesWaterAndPufferfish(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack pufferfishBucket = world.itematic$createStack(ItemKeys.PUFFERFISH_BUCKET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, Vec3d.ofBottomCenter(context.getAbsolutePos(FACE_POSITION)));
        player.setStackInHand(Hand.MAIN_HAND, pufferfishBucket);
        world.spawnEntity(player);
        pufferfishBucket.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> {
            Assert.fluidState(context, PLACED_POSITION)
                .is(Fluids.WATER);
            context.expectEntityAt(EntityType.PUFFERFISH, PLACED_POSITION);
        });
    }
}
