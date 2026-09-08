package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;

public class PowderSnowCauldronBlockTestSuite {
    private static final BlockPos POWDER_SNOW_CAULDRON_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.powder_snow_cauldron")
    public void usingWaterBucketOnPowderSnowCauldronFillsItWithWater(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.WATER_BUCKET)
        );
        helper.useBlock(POWDER_SNOW_CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, POWDER_SNOW_CAULDRON_POSITION)
                .is(Blocks.WATER_CAULDRON)
                .hasProperty(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.BUCKET);
        });
    }

    @GameTest(structure = "itematic:block.powder_snow_cauldron")
    public void usingLavaBucketOnPowderSnowCauldronFillsItWithLava(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.LAVA_BUCKET)
        );
        helper.useBlock(POWDER_SNOW_CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, POWDER_SNOW_CAULDRON_POSITION)
                .is(Blocks.LAVA_CAULDRON);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.BUCKET);
        });
    }

    @GameTest(structure = "itematic:block.powder_snow_cauldron.with_water_above")
    public void usingLavaBucketOnPowderSnowCauldronWithWaterAboveItDoesNotFillItWithLava(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.LAVA_BUCKET)
        );
        helper.useBlock(POWDER_SNOW_CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, POWDER_SNOW_CAULDRON_POSITION)
                .is(Blocks.POWDER_SNOW_CAULDRON);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.LAVA_BUCKET);
        });
    }

    @GameTest(structure = "itematic:block.powder_snow_cauldron")
    public void usingPowderSnowBucketOnPowderSnowCauldronFillsItWithPowderSnow(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.POWDER_SNOW_BUCKET)
        );
        helper.useBlock(POWDER_SNOW_CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, POWDER_SNOW_CAULDRON_POSITION)
                .is(Blocks.POWDER_SNOW_CAULDRON)
                .hasProperty(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.BUCKET);
        });
    }

    @GameTest(structure = "itematic:block.powder_snow_cauldron.with_water_above")
    public void usingPowderSnowBucketOnPowderSnowCauldronWithWaterAboveItDoesNotFillItWithPowderSnow(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.POWDER_SNOW_BUCKET)
        );
        helper.useBlock(POWDER_SNOW_CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, POWDER_SNOW_CAULDRON_POSITION)
                .is(Blocks.POWDER_SNOW_CAULDRON);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.POWDER_SNOW_BUCKET);
        });
    }
}
