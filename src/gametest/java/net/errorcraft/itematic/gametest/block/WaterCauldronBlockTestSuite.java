package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
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
    public void usingColoredShulkerBoxOnWaterCauldronClearsColor(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        ItemStack whiteShulkerBox = world.itematic$createStack(ItemKeys.WHITE_SHULKER_BOX);
        player.setItemInHand(InteractionHand.MAIN_HAND, whiteShulkerBox);
        world.addFreshEntity(player);
        context.useBlock(WATER_CAULDRON_POSITION, player);
        context.succeedIf(() -> Assert.itemStack(context, player.getItemInHand(InteractionHand.MAIN_HAND))
            .is(ItemKeys.SHULKER_BOX));
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingColoredWolfArmorOnWaterCauldronClearsColor(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        ItemStack wolfArmor = world.itematic$createStack(ItemKeys.WOLF_ARMOR);
        wolfArmor.set(DataComponents.DYED_COLOR, new DyedItemColor(0xffffff));
        player.setItemInHand(InteractionHand.MAIN_HAND, wolfArmor);
        world.addFreshEntity(player);
        context.useBlock(WATER_CAULDRON_POSITION, player);
        context.succeedIf(() -> Assert.itemStack(context, player.getItemInHand(InteractionHand.MAIN_HAND))
            .doesNotHaveComponent(DataComponents.DYED_COLOR));
    }
}
