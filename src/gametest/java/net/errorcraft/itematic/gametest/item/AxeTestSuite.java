package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.references.ItemIds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

public class AxeTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.axe.platform.oak_log")
    public void usingAxeOnLogStripsIt(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.IRON_AXE)
        );
        TestUtil.interactWithBlock(helper, BLOCK_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.blockState(helper, BLOCK_POSITION)
            .is(Blocks.STRIPPED_OAK_LOG));
    }

    @GameTest(structure = "itematic:item.axe.platform.oak_wood")
    public void usingAxeOnWoodStripsIt(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.IRON_AXE)
        );
        TestUtil.interactWithBlock(helper, BLOCK_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.blockState(helper, BLOCK_POSITION)
            .is(Blocks.STRIPPED_OAK_WOOD));
    }

    @GameTest(structure = "itematic:item.axe.platform.oxidized_copper")
    public void usingAxeOnOxidizedCopperScrapesOneOxidizationLayerOff(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.IRON_AXE)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, BLOCK_POSITION, player, Direction.UP);
            Assert.blockState(helper, BLOCK_POSITION)
                .is(Blocks.COPPER_BLOCK.weathering().weathered());
            TestUtil.interactWithBlock(helper, BLOCK_POSITION, player, Direction.UP);
            Assert.blockState(helper, BLOCK_POSITION)
                .is(Blocks.COPPER_BLOCK.weathering().exposed());
            TestUtil.interactWithBlock(helper, BLOCK_POSITION, player, Direction.UP);
            Assert.blockState(helper, BLOCK_POSITION)
                .is(Blocks.COPPER_BLOCK.weathering().unaffected());
        });
    }

    @GameTest(structure = "itematic:item.axe.platform.waxed_copper_block")
    public void usingAxeOnWaxedBlockUnwaxesIt(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.IRON_AXE)
        );
        TestUtil.interactWithBlock(helper, BLOCK_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.blockState(helper, BLOCK_POSITION)
            .is(Blocks.COPPER_BLOCK.weathering().unaffected()));
    }
}
