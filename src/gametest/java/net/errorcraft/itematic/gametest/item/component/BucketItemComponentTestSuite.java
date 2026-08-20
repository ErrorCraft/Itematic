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
    public void usingBucketOnWaterTakesWaterAndGivesWaterBucket(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack bucket = level.itematic$createStack(ItemIds.BUCKET);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(helper.absolutePos(FACE_POSITION)));
        player.setItemInHand(InteractionHand.MAIN_HAND, bucket);
        level.addFreshEntity(player);
        InteractionResult result = bucket.use(level, player, InteractionHand.MAIN_HAND);
        helper.succeedIf(() -> {
            Assert.fluidState(helper, PLACED_POSITION)
                .is(Fluids.EMPTY);
            Assert.isInstance(
                helper,
                result,
                InteractionResult.Success.class,
                () -> "Expected Bucket usage to be successful",
                success -> Assert.itemStack(helper, success.heldItemTransformedTo())
                    .is(ItemIds.WATER_BUCKET)
            );
        });
    }

    @GameTest(structure = "itematic:item.component.bucket.platform.powder_snow")
    public void usingBucketOnPowderSnowTakesWaterAndGivesPowderSnowBucket(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack bucket = level.itematic$createStack(ItemIds.BUCKET);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(helper.absolutePos(FACE_POSITION)));
        player.setItemInHand(InteractionHand.MAIN_HAND, bucket);
        level.addFreshEntity(player);
        InteractionResult result = bucket.use(level, player, InteractionHand.MAIN_HAND);
        helper.succeedIf(() -> {
            Assert.fluidState(helper, PLACED_POSITION)
                .is(Fluids.EMPTY);
            Assert.isInstance(
                helper,
                result,
                InteractionResult.Success.class,
                () -> "Expected Bucket usage to be successful",
                success -> Assert.itemStack(helper, success.heldItemTransformedTo())
                    .is(ItemIds.POWDER_SNOW_BUCKET)
            );
        });
    }

    @GameTest(structure = "itematic:item.component.bucket.platform")
    public void usingWaterBucketOnGroundPlacesWater(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack waterBucket = level.itematic$createStack(ItemIds.WATER_BUCKET);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(helper.absolutePos(FACE_POSITION)));
        player.setItemInHand(InteractionHand.MAIN_HAND, waterBucket);
        level.addFreshEntity(player);
        waterBucket.use(level, player, InteractionHand.MAIN_HAND);
        helper.succeedIf(() -> Assert.fluidState(helper, PLACED_POSITION)
            .is(Fluids.WATER));
    }

    @GameTest(structure = "itematic:item.component.bucket.platform")
    public void usingPowderSnowBucketOnGroundPlacesPowderSnow(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack powderSnowBucket = level.itematic$createStack(ItemIds.POWDER_SNOW_BUCKET);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(helper.absolutePos(FACE_POSITION)));
        player.setItemInHand(InteractionHand.MAIN_HAND, powderSnowBucket);
        level.addFreshEntity(player);
        powderSnowBucket.use(level, player, InteractionHand.MAIN_HAND);
        helper.succeedIf(() -> Assert.blockState(helper, PLACED_POSITION)
            .is(Blocks.POWDER_SNOW));
    }

    @GameTest(structure = "itematic:item.component.bucket.platform")
    public void usingPufferfishBucketOnGroundPlacesWaterAndPufferfish(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack pufferfishBucket = level.itematic$createStack(ItemIds.PUFFERFISH_BUCKET);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, SPAWN_POSITION);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(helper.absolutePos(FACE_POSITION)));
        player.setItemInHand(InteractionHand.MAIN_HAND, pufferfishBucket);
        level.addFreshEntity(player);
        pufferfishBucket.use(level, player, InteractionHand.MAIN_HAND);
        helper.succeedIf(() -> {
            Assert.fluidState(helper, PLACED_POSITION)
                .is(Fluids.WATER);
            helper.assertEntityPresent(EntityType.PUFFERFISH, PLACED_POSITION);
        });
    }
}
