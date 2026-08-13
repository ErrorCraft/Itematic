package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

public class BucketItemComponentTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 2, 0);
    private static final BlockPos FACE_POSITION = new BlockPos(1, 1, 1);
    private static final BlockPos PLACED_POSITION = FACE_POSITION;

    @GameTest(structure = "itematic:item.component.bucket.platform.water")
    public void usingBucketOnWaterTakesWaterAndGivesWaterBucket(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack bucket = world.itematic$createStack(ItemIds.BUCKET);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(context.absolutePos(FACE_POSITION)));
        player.setItemInHand(InteractionHand.MAIN_HAND, bucket);
        world.addFreshEntity(player);
        InteractionResult result = bucket.use(world, player, InteractionHand.MAIN_HAND);
        context.succeedIf(() -> {
            Assert.fluidState(context, PLACED_POSITION)
                .is(Fluids.EMPTY);
            Assert.isInstance(
                context,
                result,
                InteractionResult.Success.class,
                () -> "Expected Bucket usage to be successful",
                success -> Assert.itemStack(context, success.heldItemTransformedTo())
                    .is(ItemIds.WATER_BUCKET)
            );
        });
    }

    @GameTest(structure = "itematic:item.component.bucket.platform.powder_snow")
    public void usingBucketOnPowderSnowTakesWaterAndGivesPowderSnowBucket(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack bucket = world.itematic$createStack(ItemIds.BUCKET);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(context.absolutePos(FACE_POSITION)));
        player.setItemInHand(InteractionHand.MAIN_HAND, bucket);
        world.addFreshEntity(player);
        InteractionResult result = bucket.use(world, player, InteractionHand.MAIN_HAND);
        context.succeedIf(() -> {
            Assert.fluidState(context, PLACED_POSITION)
                .is(Fluids.EMPTY);
            Assert.isInstance(
                context,
                result,
                InteractionResult.Success.class,
                () -> "Expected Bucket usage to be successful",
                success -> Assert.itemStack(context, success.heldItemTransformedTo())
                    .is(ItemIds.POWDER_SNOW_BUCKET)
            );
        });
    }

    @GameTest(structure = "itematic:item.component.bucket.platform")
    public void usingWaterBucketOnGroundPlacesWater(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack waterBucket = world.itematic$createStack(ItemIds.WATER_BUCKET);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(context.absolutePos(FACE_POSITION)));
        player.setItemInHand(InteractionHand.MAIN_HAND, waterBucket);
        world.addFreshEntity(player);
        waterBucket.use(world, player, InteractionHand.MAIN_HAND);
        context.succeedIf(() -> Assert.fluidState(context, PLACED_POSITION)
            .is(Fluids.WATER));
    }

    @GameTest(structure = "itematic:item.component.bucket.platform")
    public void usingPowderSnowBucketOnGroundPlacesPowderSnow(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack powderSnowBucket = world.itematic$createStack(ItemIds.POWDER_SNOW_BUCKET);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(context.absolutePos(FACE_POSITION)));
        player.setItemInHand(InteractionHand.MAIN_HAND, powderSnowBucket);
        world.addFreshEntity(player);
        powderSnowBucket.use(world, player, InteractionHand.MAIN_HAND);
        context.succeedIf(() -> Assert.blockState(context, PLACED_POSITION)
            .is(Blocks.POWDER_SNOW));
    }

    @GameTest(structure = "itematic:item.component.bucket.platform")
    public void usingPufferfishBucketOnGroundPlacesWaterAndPufferfish(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack pufferfishBucket = world.itematic$createStack(ItemIds.PUFFERFISH_BUCKET);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(context.absolutePos(FACE_POSITION)));
        player.setItemInHand(InteractionHand.MAIN_HAND, pufferfishBucket);
        world.addFreshEntity(player);
        pufferfishBucket.use(world, player, InteractionHand.MAIN_HAND);
        context.succeedIf(() -> {
            Assert.fluidState(context, PLACED_POSITION)
                .is(Fluids.WATER);
            context.assertEntityPresent(EntityType.PUFFERFISH, PLACED_POSITION);
        });
    }
}
