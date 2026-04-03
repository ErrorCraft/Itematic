package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class AnvilBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.anvil")
    public void combiningEnchantedItemsWithSameIdInAnvilCombinesEnchantments(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        AnvilScreenHandler anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ANVIL);
        anvilMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.EFFICIENCY));
        context.addFinalTask(() -> Assert.itemStack(context, anvilMenu.getSlot(2).getStack())
            .is(ItemKeys.IRON_PICKAXE)
            .hasEnchantments(Enchantments.UNBREAKING, Enchantments.EFFICIENCY));
    }

    @GameTest(structure = "itematic:block.anvil")
    public void combiningEnchantedItemsWithDifferentIdsInAnvilRejectsCombination(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        AnvilScreenHandler anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ANVIL);
        anvilMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.DIAMOND_PICKAXE, Enchantments.EFFICIENCY));
        context.addFinalTask(() -> Assert.itemStack(context, anvilMenu.getSlot(2).getStack())
            .isEmpty());
    }

    @GameTest(structure = "itematic:block.anvil")
    public void combiningItemWithEnchantedBookInAnvilAddsEnchantment(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        AnvilScreenHandler anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ANVIL);
        anvilMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.EFFICIENCY));
        context.addFinalTask(() -> Assert.itemStack(context, anvilMenu.getSlot(2).getStack())
            .is(ItemKeys.IRON_PICKAXE)
            .hasEnchantments(Enchantments.UNBREAKING, Enchantments.EFFICIENCY));
    }

    @GameTest(structure = "itematic:block.anvil")
    public void combiningItemWithEnchantedBookWithIncompatibleEnchantmentInAnvilRejectsCombination(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        AnvilScreenHandler anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ANVIL);
        anvilMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.SHARPNESS));
        context.addFinalTask(() -> Assert.itemStack(context, anvilMenu.getSlot(2).getStack())
            .isEmpty());
    }

    @GameTest(structure = "itematic:block.anvil")
    public void combiningEnchantedBooksInAnvilCombinesEnchantments(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        AnvilScreenHandler anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ANVIL);
        anvilMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.EFFICIENCY));
        context.addFinalTask(() -> Assert.itemStack(context, anvilMenu.getSlot(2).getStack())
            .is(ItemKeys.ENCHANTED_BOOK)
            .hasEnchantments(Enchantments.UNBREAKING, Enchantments.EFFICIENCY));
    }
}
