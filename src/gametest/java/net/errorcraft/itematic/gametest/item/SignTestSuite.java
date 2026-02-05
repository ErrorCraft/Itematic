package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class SignTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 0);
    private static final BlockPos PLACED_BLOCK_POSITION = GROUND_POSITION.add(0, 1, 0);
    private static final BlockPos ABOVE_PLACED_BLOCK_POSITION = PLACED_BLOCK_POSITION.add(0, 1, 0);

    @GameTest(templateName = "itematic:item.sign.platform")
    public void placingSignOpensSignMenu(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OAK_SIGN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            context.expectBlock(Blocks.OAK_SIGN, PLACED_BLOCK_POSITION);
            Assert.blockEntityExists(context, PLACED_BLOCK_POSITION, BlockEntityType.SIGN, blockEntity -> {
                if (!player.getUuid().equals(blockEntity.getEditor())) {
                    throw new GameTestException("Sign menu was not opened by the player");
                }
            });
        });
    }

    @GameTest(templateName = "itematic:item.sign.platform")
    public void placingSignWithBlockEntityDataDoesNotOpenSignMenu(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OAK_SIGN);
        NbtComponent.set(DataComponentTypes.BLOCK_ENTITY_DATA, stack, nbt -> nbt.putString(Entity.ID_KEY, Registries.BLOCK_ENTITY_TYPE.getId(BlockEntityType.SIGN).toString()));
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            context.expectBlock(Blocks.OAK_SIGN, PLACED_BLOCK_POSITION);
            Assert.blockEntityExists(context, PLACED_BLOCK_POSITION, BlockEntityType.SIGN, blockEntity -> {
                if (player.getUuid().equals(blockEntity.getEditor())) {
                    throw new GameTestException("Sign menu was opened by the player");
                }
            });
        });
    }

    @GameTest(templateName = "itematic:item.sign.platform.ceiling")
    public void placingHangingSignOpensSignMenu(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OAK_HANGING_SIGN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, ABOVE_PLACED_BLOCK_POSITION, Direction.DOWN);
        context.addInstantFinalTask(() -> {
            context.expectBlock(Blocks.OAK_HANGING_SIGN, PLACED_BLOCK_POSITION);
            Assert.blockEntityExists(context, PLACED_BLOCK_POSITION, BlockEntityType.HANGING_SIGN, blockEntity -> {
                if (!player.getUuid().equals(blockEntity.getEditor())) {
                    throw new GameTestException("Sign menu was not opened by the player");
                }
            });
        });
    }

    @GameTest(templateName = "itematic:item.sign.platform.ceiling")
    public void placingHangingSignWithBlockEntityDataDoesNotOpenSignMenu(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OAK_HANGING_SIGN);
        NbtComponent.set(DataComponentTypes.BLOCK_ENTITY_DATA, stack, nbt -> nbt.putString(Entity.ID_KEY, Registries.BLOCK_ENTITY_TYPE.getId(BlockEntityType.SIGN).toString()));
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, ABOVE_PLACED_BLOCK_POSITION, Direction.DOWN);
        context.addInstantFinalTask(() -> {
            context.expectBlock(Blocks.OAK_HANGING_SIGN, PLACED_BLOCK_POSITION);
            Assert.blockEntityExists(context, PLACED_BLOCK_POSITION, BlockEntityType.HANGING_SIGN, blockEntity -> {
                if (player.getUuid().equals(blockEntity.getEditor())) {
                    throw new GameTestException("Sign menu was opened by the player");
                }
            });
        });
    }
}
