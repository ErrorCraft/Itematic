package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;

public class WaterCauldronBlockTestSuite {
    private static final BlockPos WATER_CAULDRON_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingWaterBucketOnWaterCauldronFillsItWithWater(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.WATER_BUCKET)
        );
        helper.useBlock(WATER_CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, WATER_CAULDRON_POSITION)
                .is(Blocks.WATER_CAULDRON)
                .hasProperty(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.BUCKET);
        });
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingLavaBucketOnWaterCauldronFillsItWithLava(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.LAVA_BUCKET)
        );
        helper.useBlock(WATER_CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, WATER_CAULDRON_POSITION)
                .is(Blocks.LAVA_CAULDRON);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.BUCKET);
        });
    }

    @GameTest(structure = "itematic:block.water_cauldron.with_water_above")
    public void usingLavaBucketOnWaterCauldronWithWaterAboveItDoesNotFillItWithLava(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.LAVA_BUCKET)
        );
        helper.useBlock(WATER_CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, WATER_CAULDRON_POSITION)
                .is(Blocks.WATER_CAULDRON);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.LAVA_BUCKET);
        });
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingPowderSnowBucketOnWaterCauldronFillsItWithPowderSnow(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.POWDER_SNOW_BUCKET)
        );
        helper.useBlock(WATER_CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, WATER_CAULDRON_POSITION)
                .is(Blocks.POWDER_SNOW_CAULDRON)
                .hasProperty(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.BUCKET);
        });
    }

    @GameTest(structure = "itematic:block.water_cauldron.with_water_above")
    public void usingPowderSnowBucketOnWaterCauldronWithWaterAboveItDoesNotFillItWithPowderSnow(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.POWDER_SNOW_BUCKET)
        );
        helper.useBlock(WATER_CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, WATER_CAULDRON_POSITION)
                .is(Blocks.WATER_CAULDRON);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.POWDER_SNOW_BUCKET);
        });
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingColoredShulkerBoxOnWaterCauldronClearsColor(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.WHITE_SHULKER_BOX)
        );
        helper.useBlock(WATER_CAULDRON_POSITION, player);
        helper.succeedIf(() -> Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
            .is(ItemIds.SHULKER_BOX));
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingColoredLeatherChestplateOnWaterCauldronClearsColor(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack leatherChestplate = helper.getLevel().itematic$createStack(ItemIds.LEATHER_CHESTPLATE);
        leatherChestplate.set(DataComponents.DYED_COLOR, new DyedItemColor(0xffffff));
        player.setItemInHand(InteractionHand.MAIN_HAND, leatherChestplate);
        helper.useBlock(WATER_CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, WATER_CAULDRON_POSITION)
                .hasProperty(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL - 1);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .doesNotHaveComponent(DataComponents.DYED_COLOR);
        });
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingUncoloredLeatherChestplateOnWaterCauldronDoesNotDecreaseWaterLayer(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.LEATHER_CHESTPLATE)
        );
        helper.useBlock(WATER_CAULDRON_POSITION, player);
        helper.succeedIf(() -> Assert.blockState(helper, WATER_CAULDRON_POSITION)
            .hasProperty(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL));
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingColoredWolfArmorOnWaterCauldronClearsColor(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack wolfArmor = helper.getLevel().itematic$createStack(ItemIds.WOLF_ARMOR);
        wolfArmor.set(DataComponents.DYED_COLOR, new DyedItemColor(0xffffff));
        player.setItemInHand(InteractionHand.MAIN_HAND, wolfArmor);
        helper.useBlock(WATER_CAULDRON_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, WATER_CAULDRON_POSITION)
                .hasProperty(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL - 1);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .doesNotHaveComponent(DataComponents.DYED_COLOR);
        });
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingUncoloredWolfArmorOnWaterCauldronDoesNotDecreaseWaterLayer(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.WOLF_ARMOR)
        );
        helper.useBlock(WATER_CAULDRON_POSITION, player);
        helper.succeedIf(() -> Assert.blockState(helper, WATER_CAULDRON_POSITION)
            .hasProperty(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL));
    }
}
