package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ComposterBlockTestSuite {
    private static final BlockPos COMPOSTER_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.composter.empty")
    public void usingCompostableItemOnComposterIncreasesLevel(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, context.getLevel().itematic$createStack(ItemIds.PUMPKIN_PIE));
        context.useBlock(COMPOSTER_POSITION, player);
        context.succeedIf(() -> Assert.blockState(context, COMPOSTER_POSITION)
            .hasProperty(BlockStateProperties.LEVEL_COMPOSTER, 1, () -> "Expected Composter level to increase to 1"));
    }

    @GameTest(structure = "itematic:block.composter.full")
    public void usingBlockOnFullComposterEmptiesComposterAndSpawnsBoneMeal(GameTestHelper context) {
        context.useBlock(COMPOSTER_POSITION);
        context.succeedIf(() -> {
            Assert.blockState(context, COMPOSTER_POSITION)
                .hasProperty(BlockStateProperties.LEVEL_COMPOSTER, 0, () -> "Expected Composter to be emptied");
            context.assertItemEntityPresent(context.getLevel().itematic$getItem(ItemIds.BONE_MEAL).value());
        });
    }
}
