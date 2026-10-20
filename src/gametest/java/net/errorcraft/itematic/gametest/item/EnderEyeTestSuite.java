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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class EnderEyeTestSuite {
    private static final BlockPos END_PORTAL_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.ender_eye.platform")
    public void usingEnderEyeOnEndPortalPlacesEyeInside(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.ENDER_EYE)
        );
        TestUtil.interactWithBlock(helper, END_PORTAL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.blockState(helper, END_PORTAL_POSITION)
            .hasProperty(BlockStateProperties.EYE, true));
    }

    @GameTest(structure = "itematic:item.ender_eye.platform.eye")
    public void usingEnderEyeOnEndPortalWithEyeAlreadyPlacedDoesNotConsumeEnderEye(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.ENDER_EYE)
        );
        TestUtil.interactWithBlock(helper, END_PORTAL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.livingEntity(helper, player)
            .hasStackInHand(InteractionHand.MAIN_HAND, ItemStackAssert::isNotEmpty));
    }
}
