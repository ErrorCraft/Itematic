package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;

public class EnchantingTableTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.enchanting_table")
    public void placingEnchantableItemWithoutEnchantmentsSuggestsEnchantments(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        EnchantmentMenu enchantmentMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, MenuType.ENCHANTMENT);
        enchantmentMenu.getSlot(0)
            .setByPlayer(level.itematic$createStack(ItemIds.IRON_PICKAXE));
        enchantmentMenu.getSlot(1)
            .setByPlayer(level.itematic$createStack(ItemIds.LAPIS_LAZULI));
        helper.succeedIf(() -> Assert.isTrue(
            helper,
            enchantmentMenu.costs[0] > 0,
            () -> "Expected enchantments to be suggested"
        ));
    }

    @GameTest(structure = "itematic:block.enchanting_table")
    public void placingUnenchantableItemInEnchantingTableDoesNotSuggestEnchantments(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        EnchantmentMenu enchantmentMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, MenuType.ENCHANTMENT);
        enchantmentMenu.getSlot(0)
            .setByPlayer(level.itematic$createStack(ItemIds.STICK));
        enchantmentMenu.getSlot(1)
            .setByPlayer(level.itematic$createStack(ItemIds.LAPIS_LAZULI));
        helper.succeedIf(() -> Assert.isTrue(
            helper,
            enchantmentMenu.costs[0] == 0,
            () -> "Expected no enchantments to be suggested"
        ));
    }

    @GameTest(structure = "itematic:block.enchanting_table")
    public void placingEnchantableItemWithEnchantmentsInEnchantingTableDoesNotSuggestEnchantments(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        EnchantmentMenu enchantmentMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, MenuType.ENCHANTMENT);
        enchantmentMenu.getSlot(0)
            .setByPlayer(TestUtil.createItemStackWithEnchantment(level, ItemIds.IRON_PICKAXE, Enchantments.UNBREAKING));
        enchantmentMenu.getSlot(1)
            .setByPlayer(level.itematic$createStack(ItemIds.LAPIS_LAZULI));
        helper.succeedIf(() -> Assert.isTrue(
            helper,
            enchantmentMenu.costs[0] == 0,
            () -> "Expected no enchantments to be suggested"
        ));
    }

    @GameTest(structure = "itematic:block.enchanting_table")
    public void enchantingEnchantableItemInEnchantingTableAddsEnchantments(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.experienceLevel = 1000;
        EnchantmentMenu enchantmentMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, MenuType.ENCHANTMENT);
        enchantmentMenu.getSlot(0)
            .setByPlayer(level.itematic$createStack(ItemIds.IRON_PICKAXE));
        enchantmentMenu.getSlot(1)
            .setByPlayer(level.itematic$createStack(ItemIds.LAPIS_LAZULI));
        enchantmentMenu.clickMenuButton(player, 0);
        helper.succeedIf(() -> Assert.itemStack(helper, enchantmentMenu.getSlot(0).getItem())
            .is(ItemIds.IRON_PICKAXE)
            .hasEnchantments());
    }

    @GameTest(structure = "itematic:block.enchanting_table")
    public void enchantingBookInEnchantingTableTransformsItemIntoEnchantedBookAndAddsEnchantmentsToStoredEnchantments(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.experienceLevel = 1000;
        EnchantmentMenu enchantmentMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, MenuType.ENCHANTMENT);
        enchantmentMenu.getSlot(0)
            .setByPlayer(level.itematic$createStack(ItemIds.BOOK));
        enchantmentMenu.getSlot(1)
            .setByPlayer(level.itematic$createStack(ItemIds.LAPIS_LAZULI));
        enchantmentMenu.clickMenuButton(player, 0);
        helper.succeedIf(() -> Assert.itemStack(helper, enchantmentMenu.getSlot(0).getItem())
            .is(ItemIds.ENCHANTED_BOOK)
            .hasComponent(DataComponents.ENCHANTMENTS, enchantments -> Assert.isTrue(
                helper,
                enchantments.isEmpty(),
                () -> "Expected enchantments not to be added to " + DataComponents.ENCHANTMENTS
            ))
            .hasComponent(DataComponents.STORED_ENCHANTMENTS, storedEnchantments -> Assert.isFalse(
                helper,
                storedEnchantments.isEmpty(),
                () -> "Expected enchantments to be added to " + DataComponents.STORED_ENCHANTMENTS
            )));
    }
}
