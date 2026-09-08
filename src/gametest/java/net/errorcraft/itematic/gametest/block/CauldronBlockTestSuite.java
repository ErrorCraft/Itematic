package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;

public class CauldronBlockTestSuite {
    private static final BlockPos CAULDRON_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.cauldron")
    public void usingWaterBucketOnCauldronFillsItWithWater(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.WATER_BUCKET)
        );
        level.addFreshEntity(player);
        helper.useBlock(CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, CAULDRON_POSITION)
                .is(Blocks.WATER_CAULDRON)
                .hasProperty(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.BUCKET);
        });
    }

    @GameTest(structure = "itematic:block.cauldron")
    public void usingLavaBucketOnCauldronFillsItWithLava(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.LAVA_BUCKET)
        );
        level.addFreshEntity(player);
        helper.useBlock(CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, CAULDRON_POSITION)
                .is(Blocks.LAVA_CAULDRON);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.BUCKET);
        });
    }

    @GameTest(structure = "itematic:block.cauldron")
    public void usingPowderSnowBucketOnCauldronFillsItWithPowderSnow(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.POWDER_SNOW_BUCKET)
        );
        level.addFreshEntity(player);
        helper.useBlock(CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, CAULDRON_POSITION)
                .is(Blocks.POWDER_SNOW_CAULDRON)
                .hasProperty(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.BUCKET);
        });
    }

    @GameTest(structure = "itematic:block.cauldron")
    public void usingWaterBottleOnCauldronFillsItWithOneLayerOfWater(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            PotionContentsUtil.setPotion(
                helper.getLevel().itematic$createStack(ItemIds.POTION),
                Potions.WATER
            )
        );
        helper.succeedIf(() -> {
            helper.useBlock(CAULDRON_POSITION, player);
            Assert.blockState(helper, CAULDRON_POSITION)
                .is(Blocks.WATER_CAULDRON)
                .hasProperty(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MIN_FILL_LEVEL);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.GLASS_BOTTLE);
        });
    }
}
