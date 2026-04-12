package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class SignTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 0);
    private static final BlockPos PLACED_BLOCK_POSITION = GROUND_POSITION.add(0, 1, 0);
    private static final BlockPos ABOVE_PLACED_BLOCK_POSITION = PLACED_BLOCK_POSITION.add(0, 1, 0);

    @GameTest(structure = "itematic:item.sign.platform")
    public void placingSignOpensSignMenu(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack oakSign = world.itematic$createStack(ItemKeys.OAK_SIGN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, oakSign);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakSign, GROUND_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.blockState(context, PLACED_BLOCK_POSITION)
                .is(Blocks.OAK_SIGN);
            Assert.blockEntity(context, PLACED_BLOCK_POSITION, BlockEntityType.SIGN,
                blockEntity -> Assert.isTrue(
                    context,
                    player.getUuid().equals(blockEntity.getEditor()),
                    () -> "Sign menu was not opened by the Player"
                )
            );
        });
    }

    @GameTest(structure = "itematic:item.sign.platform")
    @SuppressWarnings("DataFlowIssue")
    public void placingSignWithBlockEntityDataDoesNotOpenSignMenu(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack oakSign = world.itematic$createStack(ItemKeys.OAK_SIGN);
        NbtComponent.set(DataComponentTypes.BLOCK_ENTITY_DATA, oakSign, nbt -> nbt.putString(Entity.ID_KEY, Registries.BLOCK_ENTITY_TYPE.getId(BlockEntityType.SIGN).toString()));
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, oakSign);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakSign, GROUND_POSITION, Direction.UP);
        context.addFinalTaskWithDuration(1, () -> {
            Assert.blockState(context, PLACED_BLOCK_POSITION)
                .is(Blocks.OAK_SIGN);
            Assert.blockEntity(context, PLACED_BLOCK_POSITION, BlockEntityType.SIGN,
                blockEntity -> Assert.isFalse(
                    context,
                    player.getUuid().equals(blockEntity.getEditor()),
                    () -> "Sign menu was opened by the Player"
                )
            );
        });
    }

    @GameTest(structure = "itematic:item.sign.platform.ceiling")
    public void placingHangingSignOpensSignMenu(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack oakHangingSign = world.itematic$createStack(ItemKeys.OAK_HANGING_SIGN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, oakHangingSign);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakHangingSign, ABOVE_PLACED_BLOCK_POSITION, Direction.DOWN);
        context.addFinalTask(() -> {
            Assert.blockState(context, PLACED_BLOCK_POSITION)
                .is(Blocks.OAK_HANGING_SIGN);
            Assert.blockEntity(context, PLACED_BLOCK_POSITION, BlockEntityType.HANGING_SIGN,
                blockEntity -> Assert.isTrue(
                    context,
                    player.getUuid().equals(blockEntity.getEditor()),
                    () -> "Sign menu was not opened by the Player"
                )
            );
        });
    }

    @GameTest(structure = "itematic:item.sign.platform.ceiling")
    @SuppressWarnings("DataFlowIssue")
    public void placingHangingSignWithBlockEntityDataDoesNotOpenSignMenu(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack oakHangingSign = world.itematic$createStack(ItemKeys.OAK_HANGING_SIGN);
        NbtComponent.set(DataComponentTypes.BLOCK_ENTITY_DATA, oakHangingSign, nbt -> nbt.putString(Entity.ID_KEY, Registries.BLOCK_ENTITY_TYPE.getId(BlockEntityType.SIGN).toString()));
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, oakHangingSign);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakHangingSign, ABOVE_PLACED_BLOCK_POSITION, Direction.DOWN);
        context.addFinalTaskWithDuration(1, () -> {
            Assert.blockState(context, PLACED_BLOCK_POSITION)
                .is(Blocks.OAK_HANGING_SIGN);
            Assert.blockEntity(context, PLACED_BLOCK_POSITION, BlockEntityType.HANGING_SIGN,
                blockEntity -> Assert.isFalse(
                    context,
                    player.getUuid().equals(blockEntity.getEditor()),
                    () -> "Sign menu was opened by the Player"
                )
            );
        });
    }
}
