package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class GrindstoneTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 2, 1);

    @GameTest(templateName = "itematic:block.grindstone")
    public void placingEnchantedItemInGrindstoneDisenchantsItem(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        GrindstoneScreenHandler grindstoneMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.GRINDSTONE);
        grindstoneMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        context.addInstantFinalTask(() -> {
            ItemStack result = grindstoneMenu.getSlot(2).getStack();
            Assert.itemStackIsOf(result, ItemKeys.IRON_PICKAXE);
            Assert.itemStackHasDataComponent(result, DataComponentTypes.ENCHANTMENTS, enchantments -> {
                context.assertTrue(enchantments.isEmpty(), "Expected item to not have any enchantments");
            });
        });
    }

    @GameTest(templateName = "itematic:block.grindstone")
    public void placingEnchantedBookInGrindstoneTransformsItemIntoBook(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        GrindstoneScreenHandler grindstoneMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.GRINDSTONE);
        grindstoneMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.UNBREAKING));
        context.addInstantFinalTask(() -> {
            ItemStack result = grindstoneMenu.getSlot(2).getStack();
            Assert.itemStackIsOf(result, ItemKeys.BOOK);
            Assert.itemStackHasDataComponent(result, DataComponentTypes.ENCHANTMENTS, enchantments -> {
                context.assertTrue(enchantments.isEmpty(), "Expected item to not have any enchantments");
            });
        });
    }

    @GameTest(templateName = "itematic:block.grindstone")
    public void placingDamageableItemsWithSameIsInGrindstoneRepairsItem(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        GrindstoneScreenHandler grindstoneMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.GRINDSTONE);
        grindstoneMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithSlightDamage(world, ItemKeys.IRON_PICKAXE));
        grindstoneMenu.getSlot(1)
            .setStack(TestUtil.createItemStackWithSlightDamage(world, ItemKeys.IRON_PICKAXE));
        context.addInstantFinalTask(() -> {
            ItemStack result = grindstoneMenu.getSlot(2).getStack();
            Assert.itemStackIsOf(result, ItemKeys.IRON_PICKAXE);
            context.assertFalse(result.isDamaged(), "Expected item stack not to be damaged");
        });
    }

    @GameTest(templateName = "itematic:block.grindstone")
    public void placingDamageableItemsWithDifferentIdsInGrindstoneRejectsRepair(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        GrindstoneScreenHandler grindstoneMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.GRINDSTONE);
        grindstoneMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithSlightDamage(world, ItemKeys.IRON_PICKAXE));
        grindstoneMenu.getSlot(1)
            .setStack(TestUtil.createItemStackWithSlightDamage(world, ItemKeys.DIAMOND_PICKAXE));
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(grindstoneMenu.getSlot(2).getStack());
        });
    }
}
