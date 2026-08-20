package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class UseableOnFluidItemComponentTestSuite {
    private static final BlockPos SPAWN_POSITION_ON_LAND = new BlockPos(2, 5, 0);
    private static final BlockPos SPAWN_POSITION_IN_WATER = new BlockPos(2, 1, 2);
    private static final BlockPos LOOK_AT_WATER_POSITION_ON_LAND = SPAWN_POSITION_ON_LAND.offset(0, -1, 1);
    private static final BlockPos ABOVE_LOOK_AT_WATER_POSITION_ON_LAND = LOOK_AT_WATER_POSITION_ON_LAND.offset(0, 1, 0);
    private static final BlockPos LOOK_AT_AIR_POSITION_ON_LAND = SPAWN_POSITION_ON_LAND.offset(0, 1, 4);
    private static final BlockPos LOOK_AT_WATER_POSITION_IN_WATER = SPAWN_POSITION_IN_WATER.offset(0, -1, 1);
    private static final BlockPos ABOVE_LOOK_AT_WATER_POSITION_IN_WATER = LOOK_AT_WATER_POSITION_IN_WATER.offset(0, 1, 0);
    private static final BlockPos LOOK_AT_AIR_POSITION_IN_WATER = SPAWN_POSITION_IN_WATER.offset(0, 3, 0);

    @GameTest(structure = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingLilyPadWhileLookingAtWaterPlacesLilyPad(GameTestHelper helper) {
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION_ON_LAND);
        player.lookAt(
            EntityAnchorArgument.Anchor.EYES,
            Vec3.atBottomCenterOf(helper.absolutePos(LOOK_AT_WATER_POSITION_ON_LAND))
        );
        ServerLevel level = helper.getLevel();
        ItemStack lilyPad = level.itematic$createStack(ItemIds.LILY_PAD);
        player.setItemInHand(InteractionHand.MAIN_HAND, lilyPad);
        helper.succeedIf(() -> {
            InteractionResult result = lilyPad.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Lily Pad usage to be successful"
            );
            Assert.blockState(helper, ABOVE_LOOK_AT_WATER_POSITION_ON_LAND)
                .is(Blocks.LILY_PAD);
        });
    }

    @GameTest(structure = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingPigSpawnEggWhileLookingAtWaterSpawnsPigAtWater(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, SPAWN_POSITION_ON_LAND);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(helper.absolutePos(LOOK_AT_WATER_POSITION_ON_LAND)));
        ServerLevel level = helper.getLevel();
        ItemStack pigSpawnEgg = level.itematic$createStack(ItemIds.PIG_SPAWN_EGG);
        player.setItemInHand(InteractionHand.MAIN_HAND, pigSpawnEgg);
        helper.succeedIf(() -> {
            InteractionResult result = pigSpawnEgg.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Pig Spawn Egg usage to be successful"
            );
            helper.assertEntityPresent(EntityType.PIG, LOOK_AT_WATER_POSITION_ON_LAND);
        });
    }

    @GameTest(structure = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingPigSpawnEggWhileLookingAtAirDoesNotSpawnPig(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, SPAWN_POSITION_ON_LAND);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(helper.absolutePos(LOOK_AT_AIR_POSITION_ON_LAND)));
        ServerLevel level = helper.getLevel();
        ItemStack pigSpawnEgg = level.itematic$createStack(ItemIds.PIG_SPAWN_EGG);
        player.setItemInHand(InteractionHand.MAIN_HAND, pigSpawnEgg);
        helper.succeedIf(() -> {
            InteractionResult result = pigSpawnEgg.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isFalse(
                helper,
                result.consumesAction(),
                () -> "Expected Pig Spawn Egg usage to be unsuccessful"
            );
            helper.assertEntityNotPresent(EntityType.PIG);
        });
    }

    @GameTest(structure = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingPigSpawnEggWhileLookingAtGroundUnderWaterSpawnsPigOnGroundUnderWater(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, SPAWN_POSITION_IN_WATER);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(helper.absolutePos(LOOK_AT_WATER_POSITION_IN_WATER)));
        ServerLevel level = helper.getLevel();
        ItemStack pigSpawnEgg = level.itematic$createStack(ItemIds.PIG_SPAWN_EGG);
        player.setItemInHand(InteractionHand.MAIN_HAND, pigSpawnEgg);
        TestUtil.interactWithBlock(helper, ABOVE_LOOK_AT_WATER_POSITION_IN_WATER, player, Direction.DOWN);
        helper.succeedIf(() -> helper.assertEntityPresent(EntityType.PIG, ABOVE_LOOK_AT_WATER_POSITION_IN_WATER));
    }

    @GameTest(structure = "itematic:item.component.useable_on_fluid.water_hole")
    public void usingPigSpawnEggWhileLookingAtWaterUnderWaterSpawnsPigAtPlayerEyes(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, SPAWN_POSITION_IN_WATER);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atBottomCenterOf(helper.absolutePos(LOOK_AT_AIR_POSITION_IN_WATER)));
        ServerLevel level = helper.getLevel();
        ItemStack pigSpawnEgg = level.itematic$createStack(ItemIds.PIG_SPAWN_EGG);
        player.setItemInHand(InteractionHand.MAIN_HAND, pigSpawnEgg);
        InteractionResult result = pigSpawnEgg.use(level, player, InteractionHand.MAIN_HAND);
        helper.succeedIf(() -> {
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Pig Spawn Egg usage to be successful"
            );
            BlockPos eyeBlockPos = SPAWN_POSITION_IN_WATER.offset(0, (int) player.getEyeHeight(), 0);
            helper.assertEntityPresent(EntityType.PIG, eyeBlockPos);
        });
    }
}
