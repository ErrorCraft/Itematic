package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;

public class GrindstoneTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.grindstone")
    public void placingEnchantedItemInGrindstoneDisenchantsItem(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        GrindstoneMenu grindstoneMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, MenuType.GRINDSTONE);
        grindstoneMenu.getSlot(0).setByPlayer(
            TestUtil.createItemStackWithEnchantment(world, ItemIds.IRON_PICKAXE, Enchantments.UNBREAKING)
        );
        context.succeedIf(() -> Assert.itemStack(context, grindstoneMenu.getSlot(2).getItem())
            .is(ItemIds.IRON_PICKAXE)
            .hasNoEnchantments());
    }

    @GameTest(structure = "itematic:block.grindstone")
    public void placingEnchantedBookInGrindstoneTransformsItemIntoBook(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        GrindstoneMenu grindstoneMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, MenuType.GRINDSTONE);
        grindstoneMenu.getSlot(0).setByPlayer(
            TestUtil.createItemStackWithEnchantment(world, ItemIds.ENCHANTED_BOOK, Enchantments.UNBREAKING)
        );
        context.succeedIf(() -> Assert.itemStack(context, grindstoneMenu.getSlot(2).getItem())
            .is(ItemIds.BOOK)
            .hasNoEnchantments());
    }

    @GameTest(structure = "itematic:block.grindstone")
    public void placingDamageableItemsWithSameIsInGrindstoneRepairsItem(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        GrindstoneMenu grindstoneMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, MenuType.GRINDSTONE);
        grindstoneMenu.getSlot(0).setByPlayer(
            TestUtil.createItemStackWithSlightDamage(world, ItemIds.IRON_PICKAXE)
        );
        grindstoneMenu.getSlot(1).setByPlayer(
            TestUtil.createItemStackWithSlightDamage(world, ItemIds.IRON_PICKAXE)
        );
        context.succeedIf(() -> Assert.itemStack(context, grindstoneMenu.getSlot(2).getItem())
            .is(ItemIds.IRON_PICKAXE)
            .isNotDamaged());
    }

    @GameTest(structure = "itematic:block.grindstone")
    public void placingDamageableItemsWithDifferentIdsInGrindstoneRejectsRepair(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        GrindstoneMenu grindstoneMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, MenuType.GRINDSTONE);
        grindstoneMenu.getSlot(0).setByPlayer(
            TestUtil.createItemStackWithSlightDamage(world, ItemIds.IRON_PICKAXE)
        );
        grindstoneMenu.getSlot(1).setByPlayer(
            TestUtil.createItemStackWithSlightDamage(world, ItemIds.DIAMOND_PICKAXE)
        );
        context.succeedIf(() -> Assert.itemStack(context, grindstoneMenu.getSlot(2).getItem())
            .isEmpty());
    }
}
