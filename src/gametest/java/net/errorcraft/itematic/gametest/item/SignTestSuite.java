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

public class SignTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 0);
    private static final BlockPos PLACED_BLOCK_POSITION = GROUND_POSITION.offset(0, 1, 0);
    private static final BlockPos ABOVE_PLACED_BLOCK_POSITION = PLACED_BLOCK_POSITION.offset(0, 1, 0);

    @GameTest(structure = "itematic:item.sign.platform")
    public void placingSignOpensSignMenu(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setXRot(90.0f);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemIds.OAK_SIGN));
        world.addFreshEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.blockState(context, PLACED_BLOCK_POSITION)
                .is(Blocks.OAK_SIGN);
            Assert.blockEntity(context, PLACED_BLOCK_POSITION, BlockEntityType.SIGN,
                blockEntity -> Assert.isTrue(
                    context,
                    player.getUUID().equals(blockEntity.getPlayerWhoMayEdit()),
                    () -> "Sign menu was not opened by the Player"
                )
            );
        });
    }

    @GameTest(structure = "itematic:item.sign.platform")
    public void placingSignWithBlockEntityDataDoesNotOpenSignMenu(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setXRot(90.0f);
        ItemStack oakSign = world.itematic$createStack(ItemIds.OAK_SIGN);
        oakSign.set(
            DataComponents.BLOCK_ENTITY_DATA,
            TypedEntityData.of(
                BlockEntityType.SIGN,
                new CompoundTag()
            )
        );
        player.setItemInHand(InteractionHand.MAIN_HAND, oakSign);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.succeedOnTickWhen(1, () -> {
            Assert.blockState(context, PLACED_BLOCK_POSITION)
                .is(Blocks.OAK_SIGN);
            Assert.blockEntity(context, PLACED_BLOCK_POSITION, BlockEntityType.SIGN,
                blockEntity -> Assert.isFalse(
                    context,
                    player.getUUID().equals(blockEntity.getPlayerWhoMayEdit()),
                    () -> "Sign menu was opened by the Player"
                )
            );
        });
    }

    @GameTest(structure = "itematic:item.sign.platform.ceiling")
    public void placingHangingSignOpensSignMenu(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setXRot(-90.0f);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemIds.OAK_HANGING_SIGN));
        world.addFreshEntity(player);
        TestUtil.useBlock(context, ABOVE_PLACED_BLOCK_POSITION, player, Direction.DOWN);
        context.succeedIf(() -> {
            Assert.blockState(context, PLACED_BLOCK_POSITION)
                .is(Blocks.OAK_HANGING_SIGN);
            Assert.blockEntity(context, PLACED_BLOCK_POSITION, BlockEntityType.HANGING_SIGN,
                blockEntity -> Assert.isTrue(
                    context,
                    player.getUUID().equals(blockEntity.getPlayerWhoMayEdit()),
                    () -> "Sign menu was not opened by the Player"
                )
            );
        });
    }

    @GameTest(structure = "itematic:item.sign.platform.ceiling")
    public void placingHangingSignWithBlockEntityDataDoesNotOpenSignMenu(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setXRot(-90.0f);
        ItemStack oakHangingSign = world.itematic$createStack(ItemIds.OAK_HANGING_SIGN);
        oakHangingSign.set(
            DataComponents.BLOCK_ENTITY_DATA,
            TypedEntityData.of(
                BlockEntityType.SIGN,
                new CompoundTag()
            )
        );
        player.setItemInHand(InteractionHand.MAIN_HAND, oakHangingSign);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, ABOVE_PLACED_BLOCK_POSITION, player, Direction.DOWN);
        context.succeedOnTickWhen(1, () -> {
            Assert.blockState(context, PLACED_BLOCK_POSITION)
                .is(Blocks.OAK_HANGING_SIGN);
            Assert.blockEntity(context, PLACED_BLOCK_POSITION, BlockEntityType.HANGING_SIGN,
                blockEntity -> Assert.isFalse(
                    context,
                    player.getUUID().equals(blockEntity.getPlayerWhoMayEdit()),
                    () -> "Sign menu was opened by the Player"
                )
            );
        });
    }
}
