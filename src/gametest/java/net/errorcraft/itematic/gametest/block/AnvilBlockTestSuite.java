package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class AnvilBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 2, 1);

    @GameTest(templateName = "itematic:block.anvil")
    public void combiningEnchantedItemsWithSameIdInAnvilCombinesEnchantments(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        AnvilScreenHandler anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ANVIL);
        anvilMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.EFFICIENCY));
        context.addInstantFinalTask(() -> {
            ItemStack result = anvilMenu.getSlot(2).getStack();
            Assert.itemStackIsOf(result, ItemKeys.IRON_PICKAXE);
            Assert.itemStackHasDataComponent(result, DataComponentTypes.ENCHANTMENTS, enchantments -> {
                Assert.dataComponentHasEnchantment(enchantments, Enchantments.UNBREAKING);
                Assert.dataComponentHasEnchantment(enchantments, Enchantments.EFFICIENCY);
            });
        });
    }

    @GameTest(templateName = "itematic:block.anvil")
    public void combiningEnchantedItemsWithDifferentIdsInAnvilRejectsCombination(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        AnvilScreenHandler anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ANVIL);
        anvilMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.DIAMOND_PICKAXE, Enchantments.EFFICIENCY));
        context.addInstantFinalTask(() -> Assert.itemStackIsEmpty(anvilMenu.getSlot(2).getStack()));
    }

    @GameTest(templateName = "itematic:block.anvil")
    public void combiningItemWithEnchantedBookInAnvilAddsEnchantment(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        AnvilScreenHandler anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ANVIL);
        anvilMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.EFFICIENCY));
        context.addInstantFinalTask(() -> {
            ItemStack result = anvilMenu.getSlot(2).getStack();
            Assert.itemStackIsOf(result, ItemKeys.IRON_PICKAXE);
            Assert.itemStackHasDataComponent(result, DataComponentTypes.ENCHANTMENTS, enchantments -> {
                Assert.dataComponentHasEnchantment(enchantments, Enchantments.UNBREAKING);
                Assert.dataComponentHasEnchantment(enchantments, Enchantments.EFFICIENCY);
            });
        });
    }

    @GameTest(templateName = "itematic:block.anvil")
    public void combiningItemWithEnchantedBookWithIncompatibleEnchantmentInAnvilRejectsCombination(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        AnvilScreenHandler anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ANVIL);
        anvilMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.SHARPNESS));
        context.addInstantFinalTask(() -> Assert.itemStackIsEmpty(anvilMenu.getSlot(2).getStack()));
    }

    @GameTest(templateName = "itematic:block.anvil")
    public void combiningEnchantedBooksInAnvilCombinesEnchantments(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        AnvilScreenHandler anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ANVIL);
        anvilMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.EFFICIENCY));
        context.addInstantFinalTask(() -> {
            ItemStack result = anvilMenu.getSlot(2).getStack();
            Assert.itemStackIsOf(result, ItemKeys.ENCHANTED_BOOK);
            Assert.itemStackHasDataComponent(result, DataComponentTypes.STORED_ENCHANTMENTS, storedEnchantments -> {
                Assert.dataComponentHasEnchantment(storedEnchantments, Enchantments.UNBREAKING);
                Assert.dataComponentHasEnchantment(storedEnchantments, Enchantments.EFFICIENCY);
            });
        });
    }
}
