package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignTextSlot;

public class SignBlockTestSuite {
    private static final BlockPos SIGN_POSITION = new BlockPos(1, 1, 1);
    private static final BlockPos PLAYER_POSITION = SIGN_POSITION.offset(0, 0, -1);

    @GameTest(structure = "itematic:block.sign")
    public void usingDyeOnSignAddsColorToTheInteractedSide(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, PLAYER_POSITION);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.DYE.blue())
        );
        SignBlockEntity sign = TestUtil.getBlockEntity(helper, SIGN_POSITION, BlockEntityTypes.SIGN);
        sign.updateText(text -> text.withColor(DyeColor.RED), SignTextSlot.FRONT);
        sign.updateText(text -> text.withColor(DyeColor.GREEN), SignTextSlot.BACK);
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.NORTH, SIGN_POSITION);
            Assert.areEqual(
                helper,
                sign.getText(SignTextSlot.FRONT).getColor(),
                DyeColor.BLUE,
                () -> "Expected front text on Sign to be blue"
            );
            Assert.areNotEqual(
                helper,
                sign.getText(SignTextSlot.BACK).getColor(),
                DyeColor.BLUE,
                () -> "Expected back text on Sign not to be blue"
            );
        });
    }

    @GameTest(structure = "itematic:block.sign")
    public void usingGlowInkSacOnSignAddsGlowToTheInteractedSide(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, PLAYER_POSITION);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.GLOW_INK_SAC)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.NORTH, SIGN_POSITION);
            Assert.blockEntity(helper, SIGN_POSITION, BlockEntityTypes.SIGN,
                sign -> {
                    Assert.isTrue(
                        helper,
                        sign.getText(SignTextSlot.FRONT).hasGlowingText(),
                        () -> "Expected front text on Sign to be glowing"
                    );
                    Assert.isFalse(
                        helper,
                        sign.getText(SignTextSlot.BACK).hasGlowingText(),
                        () -> "Expected back text on Sign not to be glowing"
                    );
                }
            );
        });
    }

    @GameTest(structure = "itematic:block.sign")
    public void usingInkSacOnSignRemovesGlowToTheInteractedSide(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, PLAYER_POSITION);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.INK_SAC)
        );
        SignBlockEntity sign = TestUtil.getBlockEntity(helper, SIGN_POSITION, BlockEntityTypes.SIGN);
        sign.updateText(text -> text.withGlowingText(true), SignTextSlot.FRONT);
        sign.updateText(text -> text.withGlowingText(true), SignTextSlot.BACK);
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.NORTH, SIGN_POSITION);
            Assert.isFalse(
                helper,
                sign.getText(SignTextSlot.FRONT).hasGlowingText(),
                () -> "Expected front text on Sign not to be glowing"
            );
            Assert.isTrue(
                helper,
                sign.getText(SignTextSlot.BACK).hasGlowingText(),
                () -> "Expected back text on Sign to be glowing"
            );
        });
    }

    @GameTest(structure = "itematic:block.sign")
    public void usingHoneycombOnSignWaxesIt(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, PLAYER_POSITION);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.HONEYCOMB)
        );
        helper.succeedIf(() -> {
            TestUtil.interactWithBlock(helper, player, Direction.NORTH, SIGN_POSITION);
            Assert.blockEntity(helper, SIGN_POSITION, BlockEntityTypes.SIGN,
                sign -> Assert.isTrue(
                    helper,
                    sign.isWaxed(),
                    () -> "Expected Sign to be waxed"
                )
            );
        });
    }
}
