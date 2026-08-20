package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.GameType;

public class WaterCauldronBlockTestSuite {
    private static final BlockPos WATER_CAULDRON_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingColoredShulkerBoxOnWaterCauldronClearsColor(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.WHITE_SHULKER_BOX)
        );
        level.addFreshEntity(player);
        helper.useBlock(WATER_CAULDRON_POSITION, player);
        helper.succeedIf(() -> Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
            .is(ItemIds.SHULKER_BOX));
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingColoredWolfArmorOnWaterCauldronClearsColor(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        ItemStack wolfArmor = level.itematic$createStack(ItemIds.WOLF_ARMOR);
        wolfArmor.set(DataComponents.DYED_COLOR, new DyedItemColor(0xffffff));
        player.setItemInHand(InteractionHand.MAIN_HAND, wolfArmor);
        level.addFreshEntity(player);
        helper.useBlock(WATER_CAULDRON_POSITION, player);
        helper.succeedIf(() -> Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
            .doesNotHaveComponent(DataComponents.DYED_COLOR));
    }
}
