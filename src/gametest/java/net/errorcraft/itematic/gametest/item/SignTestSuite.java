package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;

public class SignTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 1);
    private static final BlockPos PLACED_GROUND_BLOCK_POSITION = GROUND_POSITION.offset(0, 1, 0);
    private static final BlockPos CEILING_POSITION = new BlockPos(1, 3, 1);
    private static final BlockPos PLACED_CEILING_BLOCK_POSITION = CEILING_POSITION.offset(0, -1, 0);
    private static final BlockPos WALL_POSITION = new BlockPos(1, 1, 2);
    private static final BlockPos PLACED_WALL_BLOCK_POSITION = WALL_POSITION.offset(0, 0, -1);
    private static final BlockPos PLAYER_POSITION = new BlockPos(1, 1, 0);

    @GameTest(structure = "itematic:item.sign.platform")
    public void placingSignOpensSignMenu(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setXRot(90.0f);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.OAK_SIGN)
        );
        level.addFreshEntity(player);
        TestUtil.interactWithBlock(helper, GROUND_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.blockState(helper, PLACED_GROUND_BLOCK_POSITION)
                .is(Blocks.OAK_SIGN);
            Assert.blockEntity(helper, PLACED_GROUND_BLOCK_POSITION, BlockEntityType.SIGN,
                blockEntity -> Assert.isTrue(
                    helper,
                    player.getUUID().equals(blockEntity.getPlayerWhoMayEdit()),
                    () -> "Sign menu was not opened by the Player"
                )
            );
        });
    }

    @GameTest(structure = "itematic:item.sign.platform")
    public void placingSignWithBlockEntityDataDoesNotOpenSignMenu(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setXRot(90.0f);
        ItemStack oakSign = level.itematic$createStack(ItemIds.OAK_SIGN);
        oakSign.set(
            DataComponents.BLOCK_ENTITY_DATA,
            TypedEntityData.of(
                BlockEntityType.SIGN,
                new CompoundTag()
            )
        );
        player.setItemInHand(InteractionHand.MAIN_HAND, oakSign);
        level.addFreshEntity(player);
        TestUtil.interactWithBlock(helper, GROUND_POSITION, player, Direction.UP);
        helper.succeedOnTickWhen(1, () -> {
            Assert.blockState(helper, PLACED_GROUND_BLOCK_POSITION)
                .is(Blocks.OAK_SIGN);
            Assert.blockEntity(helper, PLACED_GROUND_BLOCK_POSITION, BlockEntityType.SIGN,
                blockEntity -> Assert.isFalse(
                    helper,
                    player.getUUID().equals(blockEntity.getPlayerWhoMayEdit()),
                    () -> "Sign menu was opened by the Player"
                )
            );
        });
    }

    @GameTest(structure = "itematic:item.sign.platform")
    public void placingHangingSignOpensSignMenu(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setXRot(-90.0f);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.OAK_HANGING_SIGN)
        );
        level.addFreshEntity(player);
        TestUtil.interactWithBlock(helper, CEILING_POSITION, player, Direction.DOWN);
        helper.succeedIf(() -> {
            Assert.blockState(helper, PLACED_CEILING_BLOCK_POSITION)
                .is(Blocks.OAK_HANGING_SIGN);
            Assert.blockEntity(helper, PLACED_CEILING_BLOCK_POSITION, BlockEntityType.HANGING_SIGN,
                blockEntity -> Assert.isTrue(
                    helper,
                    player.getUUID().equals(blockEntity.getPlayerWhoMayEdit()),
                    () -> "Sign menu was not opened by the Player"
                )
            );
        });
    }

    @GameTest(structure = "itematic:item.sign.platform")
    public void placingHangingSignWithBlockEntityDataDoesNotOpenSignMenu(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setXRot(-90.0f);
        ItemStack oakHangingSign = level.itematic$createStack(ItemIds.OAK_HANGING_SIGN);
        oakHangingSign.set(
            DataComponents.BLOCK_ENTITY_DATA,
            TypedEntityData.of(
                BlockEntityType.SIGN,
                new CompoundTag()
            )
        );
        player.setItemInHand(InteractionHand.MAIN_HAND, oakHangingSign);
        level.addFreshEntity(player);
        TestUtil.interactWithBlock(helper, CEILING_POSITION, player, Direction.DOWN);
        helper.succeedOnTickWhen(1, () -> {
            Assert.blockState(helper, PLACED_CEILING_BLOCK_POSITION)
                .is(Blocks.OAK_HANGING_SIGN);
            Assert.blockEntity(helper, PLACED_CEILING_BLOCK_POSITION, BlockEntityType.HANGING_SIGN,
                blockEntity -> Assert.isFalse(
                    helper,
                    player.getUUID().equals(blockEntity.getPlayerWhoMayEdit()),
                    () -> "Sign menu was opened by the Player"
                )
            );
        });
    }

    @GameTest(structure = "itematic:item.sign.platform")
    public void placingSignWhileLookingSlightlyUpPlacesSignOnWall(GameTestHelper helper) {
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, PLAYER_POSITION);
        TestUtil.lookAt(helper, player, Vec3.atLowerCornerOf(WALL_POSITION).add(0.5d, 0.0d, 0.1d));
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.OAK_SIGN)
        );
        TestUtil.interactWithBlock(helper, WALL_POSITION, player, Direction.NORTH);
        helper.succeedIf(() -> Assert.blockState(helper, PLACED_WALL_BLOCK_POSITION)
            .is(Blocks.OAK_WALL_SIGN));
    }

    @GameTest(structure = "itematic:item.sign.platform")
    public void placingSignWhileLookingSlightlyDownPlacesSignOnFloor(GameTestHelper helper) {
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, PLAYER_POSITION);
        TestUtil.lookAt(helper, player, Vec3.atLowerCornerOf(WALL_POSITION).add(0.5d, 0.0d, -0.1d));
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.OAK_SIGN)
        );
        TestUtil.interactWithBlock(helper, GROUND_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.blockState(helper, PLACED_GROUND_BLOCK_POSITION)
            .is(Blocks.OAK_SIGN));
    }
}
