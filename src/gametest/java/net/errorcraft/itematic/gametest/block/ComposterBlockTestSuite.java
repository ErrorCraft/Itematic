package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class ComposterBlockTestSuite {
    private static final BlockPos COMPOSTER_POSITION = new BlockPos(1, 2, 1);

    @GameTest(templateName = "itematic:block.composter.empty")
    public void usingCompostableItemOnComposterIncreasesLevel(TestContext context) {
        PlayerEntity player = context.createMockSurvivalPlayer();
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.PUMPKIN_PIE);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        context.useBlock(COMPOSTER_POSITION, player);
        context.addInstantFinalTask(() -> context.checkBlockState(
            COMPOSTER_POSITION,
            state -> state.get(Properties.LEVEL_8) == 1,
            () -> "Composter block level did not increase to 1"
        ));
    }

    @GameTest(templateName = "itematic:block.composter.full")
    public void usingBlockOnFullComposterEmptiesComposterAndSpawnsBoneMeal(TestContext context) {
        context.useBlock(COMPOSTER_POSITION);
        context.addInstantFinalTask(() -> {
            context.checkBlockState(
                COMPOSTER_POSITION,
                state -> state.get(Properties.LEVEL_8) == 0,
                () -> "Composter block was not emptied"
            );
            context.expectItem(context.getWorld().itematic$getItem(ItemKeys.BONE_MEAL).value());
        });
    }
}
