package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;

public class AnvilBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.anvil")
    public void combiningEnchantedItemsWithSameIdInAnvilCombinesEnchantments(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        AnvilMenu anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, MenuType.ANVIL);
        anvilMenu.getSlot(0)
            .setByPlayer(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setByPlayer(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.EFFICIENCY));
        context.succeedIf(() -> Assert.itemStack(context, anvilMenu.getSlot(2).getItem())
            .is(ItemKeys.IRON_PICKAXE)
            .hasEnchantments(Enchantments.UNBREAKING, Enchantments.EFFICIENCY));
    }

    @GameTest(structure = "itematic:block.anvil")
    public void combiningEnchantedItemsWithDifferentIdsInAnvilRejectsCombination(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        AnvilMenu anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, MenuType.ANVIL);
        anvilMenu.getSlot(0)
            .setByPlayer(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setByPlayer(TestUtil.createItemStackWithEnchantment(world, ItemKeys.DIAMOND_PICKAXE, Enchantments.EFFICIENCY));
        context.succeedIf(() -> Assert.itemStack(context, anvilMenu.getSlot(2).getItem())
            .isEmpty());
    }

    @GameTest(structure = "itematic:block.anvil")
    public void combiningItemWithEnchantedBookInAnvilAddsEnchantment(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        AnvilMenu anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, MenuType.ANVIL);
        anvilMenu.getSlot(0)
            .setByPlayer(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setByPlayer(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.EFFICIENCY));
        context.succeedIf(() -> Assert.itemStack(context, anvilMenu.getSlot(2).getItem())
            .is(ItemKeys.IRON_PICKAXE)
            .hasEnchantments(Enchantments.UNBREAKING, Enchantments.EFFICIENCY));
    }

    @GameTest(structure = "itematic:block.anvil")
    public void combiningItemWithEnchantedBookWithIncompatibleEnchantmentInAnvilRejectsCombination(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        AnvilMenu anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, MenuType.ANVIL);
        anvilMenu.getSlot(0)
            .setByPlayer(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setByPlayer(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.SHARPNESS));
        context.succeedIf(() -> Assert.itemStack(context, anvilMenu.getSlot(2).getItem())
            .isEmpty());
    }

    @GameTest(structure = "itematic:block.anvil")
    public void combiningEnchantedBooksInAnvilCombinesEnchantments(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        AnvilMenu anvilMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, MenuType.ANVIL);
        anvilMenu.getSlot(0)
            .setByPlayer(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.UNBREAKING));
        anvilMenu.getSlot(1)
            .setByPlayer(TestUtil.createItemStackWithEnchantment(world, ItemKeys.ENCHANTED_BOOK, Enchantments.EFFICIENCY));
        context.succeedIf(() -> Assert.itemStack(context, anvilMenu.getSlot(2).getItem())
            .is(ItemKeys.ENCHANTED_BOOK)
            .hasEnchantments(Enchantments.UNBREAKING, Enchantments.EFFICIENCY));
    }
}
