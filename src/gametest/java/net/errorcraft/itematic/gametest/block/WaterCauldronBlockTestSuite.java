package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class WaterCauldronBlockTestSuite {
    private static final BlockPos WATER_CAULDRON_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingColoredShulkerBoxOnWaterCauldronClearsColor(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack whiteShulkerBox = world.itematic$createStack(ItemKeys.WHITE_SHULKER_BOX);
        player.setStackInHand(Hand.MAIN_HAND, whiteShulkerBox);
        world.spawnEntity(player);
        context.useBlock(WATER_CAULDRON_POSITION, player);
        context.addFinalTask(() -> Assert.itemStack(context, player.getStackInHand(Hand.MAIN_HAND))
            .is(ItemKeys.SHULKER_BOX));
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void usingColoredWolfArmorOnWaterCauldronClearsColor(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack wolfArmor = world.itematic$createStack(ItemKeys.WOLF_ARMOR);
        wolfArmor.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(0xffffff, true));
        player.setStackInHand(Hand.MAIN_HAND, wolfArmor);
        world.spawnEntity(player);
        context.useBlock(WATER_CAULDRON_POSITION, player);
        context.addFinalTask(() -> Assert.itemStack(context, player.getStackInHand(Hand.MAIN_HAND))
            .doesNotHaveComponent(DataComponentTypes.DYED_COLOR));
    }
}
