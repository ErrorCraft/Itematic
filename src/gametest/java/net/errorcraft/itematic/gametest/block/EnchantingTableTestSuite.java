package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class EnchantingTableTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.enchanting_table")
    public void placingEnchantableItemWithoutEnchantmentsSuggestsEnchantments(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        EnchantmentScreenHandler enchantmentMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ENCHANTMENT);
        enchantmentMenu.getSlot(0)
            .setStack(world.itematic$createStack(ItemKeys.IRON_PICKAXE));
        enchantmentMenu.getSlot(1)
            .setStack(world.itematic$createStack(ItemKeys.LAPIS_LAZULI));
        context.addFinalTask(() -> Assert.isTrue(
            context,
            enchantmentMenu.enchantmentPower[0] > 0,
            () -> "Expected enchantments to be suggested"
        ));
    }

    @GameTest(structure = "itematic:block.enchanting_table")
    public void placingUnenchantableItemInEnchantingTableDoesNotSuggestEnchantments(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        EnchantmentScreenHandler enchantmentMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ENCHANTMENT);
        enchantmentMenu.getSlot(0)
            .setStack(world.itematic$createStack(ItemKeys.STICK));
        enchantmentMenu.getSlot(1)
            .setStack(world.itematic$createStack(ItemKeys.LAPIS_LAZULI));
        context.addFinalTask(() -> Assert.isTrue(
            context,
            enchantmentMenu.enchantmentPower[0] == 0,
            () -> "Expected no enchantments to be suggested"
        ));
    }

    @GameTest(structure = "itematic:block.enchanting_table")
    public void placingEnchantableItemWithEnchantmentsInEnchantingTableDoesNotSuggestEnchantments(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        EnchantmentScreenHandler enchantmentMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ENCHANTMENT);
        enchantmentMenu.getSlot(0)
            .setStack(TestUtil.createItemStackWithEnchantment(world, ItemKeys.IRON_PICKAXE, Enchantments.UNBREAKING));
        enchantmentMenu.getSlot(1)
            .setStack(world.itematic$createStack(ItemKeys.LAPIS_LAZULI));
        context.addFinalTask(() -> Assert.isTrue(
            context,
            enchantmentMenu.enchantmentPower[0] == 0,
            () -> "Expected no enchantments to be suggested"
        ));
    }

    @GameTest(structure = "itematic:block.enchanting_table")
    public void enchantingEnchantableItemInEnchantingTableAddsEnchantments(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.experienceLevel = 1000;
        EnchantmentScreenHandler enchantmentMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ENCHANTMENT);
        enchantmentMenu.getSlot(0)
            .setStack(world.itematic$createStack(ItemKeys.IRON_PICKAXE));
        enchantmentMenu.getSlot(1)
            .setStack(world.itematic$createStack(ItemKeys.LAPIS_LAZULI));
        enchantmentMenu.onButtonClick(player, 0);
        context.addFinalTask(() -> Assert.itemStack(context, enchantmentMenu.getSlot(0).getStack())
            .is(ItemKeys.IRON_PICKAXE)
            .hasEnchantments());
    }

    @GameTest(structure = "itematic:block.enchanting_table")
    public void enchantingBookInEnchantingTableTransformsItemIntoEnchantedBookAndAddsEnchantmentsToStoredEnchantments(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.experienceLevel = 1000;
        EnchantmentScreenHandler enchantmentMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.ENCHANTMENT);
        enchantmentMenu.getSlot(0)
            .setStack(world.itematic$createStack(ItemKeys.BOOK));
        enchantmentMenu.getSlot(1)
            .setStack(world.itematic$createStack(ItemKeys.LAPIS_LAZULI));
        enchantmentMenu.onButtonClick(player, 0);
        context.addFinalTask(() -> Assert.itemStack(context, enchantmentMenu.getSlot(0).getStack())
            .is(ItemKeys.ENCHANTED_BOOK)
            .hasComponent(DataComponentTypes.ENCHANTMENTS, enchantments -> Assert.isTrue(
                context,
                enchantments.isEmpty(),
                () -> "Expected enchantments not to be added to " + DataComponentTypes.ENCHANTMENTS
            ))
            .hasComponent(DataComponentTypes.STORED_ENCHANTMENTS, storedEnchantments -> Assert.isFalse(
                context,
                storedEnchantments.isEmpty(),
                () -> "Expected enchantments to be added to " + DataComponentTypes.STORED_ENCHANTMENTS
            )));
    }
}
