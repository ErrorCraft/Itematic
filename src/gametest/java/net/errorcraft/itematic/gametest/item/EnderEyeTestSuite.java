package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.assertion.ItemStackAssert;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class EnderEyeTestSuite {
    private static final BlockPos END_PORTAL_FRAME_POSITION = new BlockPos(1, 1, 0);
    private static final BlockPos END_PORTAL_POSITION = new BlockPos(2, 1, 2);

    @GameTest(structure = "itematic:item.ender_eye.platform")
    public void usingEnderEyeOnEndPortalFramePlacesEyeInside(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.ENDER_EYE)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.UP, END_PORTAL_FRAME_POSITION);
            Assert.blockState(helper, END_PORTAL_FRAME_POSITION)
                .hasProperty(BlockStateProperties.EYE, true);
            Assert.livingEntity(helper, player)
                .hasStackInHand(InteractionHand.MAIN_HAND, ItemStackAssert::isEmpty);
        });
    }

    @GameTest(structure = "itematic:item.ender_eye.platform.eye")
    public void usingEnderEyeOnEndPortalFrameWithEyeAlreadyPlacedDoesNotConsumeEnderEye(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.ENDER_EYE)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.UP, END_PORTAL_FRAME_POSITION);
            Assert.livingEntity(helper, player)
                .hasStackInHand(InteractionHand.MAIN_HAND, ItemStackAssert::isNotEmpty);
        });
    }

    @GameTest(structure = "itematic:item.ender_eye.platform.almost_full")
    public void usingEnderEyeOnEndPortalFrameToCompleteTheEndPortalOpensTheEndPortal(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.ENDER_EYE)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.UP, END_PORTAL_FRAME_POSITION);
            Assert.blockState(helper, END_PORTAL_POSITION)
                .is(Blocks.END_PORTAL);
        });
    }
}
