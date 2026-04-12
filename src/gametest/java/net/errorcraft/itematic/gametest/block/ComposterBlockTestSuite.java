package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class ComposterBlockTestSuite {
    private static final BlockPos COMPOSTER_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.composter.empty")
    public void usingCompostableItemOnComposterIncreasesLevel(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, context.getWorld().itematic$createStack(ItemKeys.PUMPKIN_PIE));
        context.useBlock(COMPOSTER_POSITION, player);
        context.addFinalTask(() -> Assert.blockState(context, COMPOSTER_POSITION)
            .hasProperty(Properties.LEVEL_8, 1, () -> "Expected Composter level to increase to 1"));
    }

    @GameTest(structure = "itematic:block.composter.full")
    public void usingBlockOnFullComposterEmptiesComposterAndSpawnsBoneMeal(TestContext context) {
        context.useBlock(COMPOSTER_POSITION);
        context.addFinalTask(() -> {
            Assert.blockState(context, COMPOSTER_POSITION)
                .hasProperty(Properties.LEVEL_8, 0, () -> "Expected Composter to be emptied");
            context.expectItem(context.getWorld().itematic$getItem(ItemKeys.BONE_MEAL).value());
        });
    }
}
