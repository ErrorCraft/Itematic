package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class GrindstoneTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.grindstone")
    public void placingEnchantedItemInGrindstoneDisenchantsItem(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        GrindstoneScreenHandler grindstoneMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.GRINDSTONE);
        grindstoneMenu.getSlot(0).setStack(
            TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING)
        );
        context.addFinalTask(() -> Assert.itemStack(context, grindstoneMenu.getSlot(2).getStack())
            .is(ItemKeys.IRON_PICKAXE)
            .hasNoEnchantments());
    }

    @GameTest(structure = "itematic:block.grindstone")
    public void placingEnchantedBookInGrindstoneTransformsItemIntoBook(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        GrindstoneScreenHandler grindstoneMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.GRINDSTONE);
        grindstoneMenu.getSlot(0).setStack(
            TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.UNBREAKING)
        );
        context.addFinalTask(() -> Assert.itemStack(context, grindstoneMenu.getSlot(2).getStack())
            .is(ItemKeys.BOOK)
            .hasNoEnchantments());
    }

    @GameTest(structure = "itematic:block.grindstone")
    public void placingDamageableItemsWithSameIsInGrindstoneRepairsItem(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        GrindstoneScreenHandler grindstoneMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.GRINDSTONE);
        grindstoneMenu.getSlot(0).setStack(
            TestUtil.createItemStackWithSlightDamage(world, ItemKeys.IRON_PICKAXE)
        );
        grindstoneMenu.getSlot(1).setStack(
            TestUtil.createItemStackWithSlightDamage(world, ItemKeys.IRON_PICKAXE)
        );
        context.addFinalTask(() -> Assert.itemStack(context, grindstoneMenu.getSlot(2).getStack())
            .is(ItemKeys.IRON_PICKAXE)
            .isNotDamaged());
    }

    @GameTest(structure = "itematic:block.grindstone")
    public void placingDamageableItemsWithDifferentIdsInGrindstoneRejectsRepair(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        GrindstoneScreenHandler grindstoneMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.GRINDSTONE);
        grindstoneMenu.getSlot(0).setStack(
            TestUtil.createItemStackWithSlightDamage(world, ItemKeys.IRON_PICKAXE)
        );
        grindstoneMenu.getSlot(1).setStack(
            TestUtil.createItemStackWithSlightDamage(world, ItemKeys.DIAMOND_PICKAXE)
        );
        context.addFinalTask(() -> Assert.itemStack(context, grindstoneMenu.getSlot(2).getStack())
            .isEmpty());
    }
}
