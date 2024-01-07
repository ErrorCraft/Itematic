package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class WaterCauldronBlockTestSuite {
    private static final BlockPos WATER_CAULDRON_POSITION = new BlockPos(1, 2, 1);

    @GameTest(templateName = "itematic:block.water_cauldron")
    public void usingColoredShulkerBoxOnWaterCauldronClearsColor(TestContext context) {
        PlayerEntity player = context.createMockSurvivalPlayer();
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WHITE_SHULKER_BOX);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        context.useBlock(WATER_CAULDRON_POSITION, player);
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.SHULKER_BOX));
    }
}
