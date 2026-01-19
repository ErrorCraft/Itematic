package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.component.type.BundleContentsComponentAccessor;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.ClickType;
import net.minecraft.world.GameMode;

public class BundleTestSuite {
    private static final int SLOT_INDEX = 0;

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void addingNormalItemToBundleAddsIt(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack bundleStack = world.itematic$createStack(ItemKeys.BUNDLE);
        ItemStack addedStack = world.itematic$createStack(ItemKeys.STICK);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        PlayerInventory inventory = player.getInventory();
        inventory.insertStack(SLOT_INDEX, addedStack);
        Slot slot = new Slot(inventory, SLOT_INDEX, 0, 0);
        context.addInstantFinalTask(() -> {
            context.assertTrue(bundleStack.onStackClicked(slot, ClickType.RIGHT, player), "Expected right-clicking with Bundle to be successful");
            Assert.itemStackIsEmpty(slot.getStack());
            Assert.itemStackHasDataComponent(bundleStack, DataComponentTypes.BUNDLE_CONTENTS, bundleContents -> {
                Assert.itemStackIsOf(bundleContents.get(0), ItemKeys.STICK);
            });
        });
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void addingShulkerBoxToBundleRejectsIt(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack bundleStack = world.itematic$createStack(ItemKeys.BUNDLE);
        ItemStack addedStack = world.itematic$createStack(ItemKeys.SHULKER_BOX);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        PlayerInventory inventory = player.getInventory();
        inventory.insertStack(SLOT_INDEX, addedStack);
        Slot slot = new Slot(inventory, SLOT_INDEX, 0, 0);
        context.addInstantFinalTask(() -> {
            context.assertTrue(bundleStack.onStackClicked(slot, ClickType.RIGHT, player), "Expected right-clicking with Bundle to be successful");
            Assert.itemStackIsNotEmpty(slot.getStack());
            Assert.itemStackHasDataComponent(bundleStack, DataComponentTypes.BUNDLE_CONTENTS, bundleContents -> {
                context.assertTrue(bundleContents.isEmpty(), "Expected Bundle to be empty");
            });
        });
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void addingBundleToBundleAddsItWithPenalty(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack bundleStack = world.itematic$createStack(ItemKeys.BUNDLE);
        ItemStack addedStack = world.itematic$createStack(ItemKeys.BUNDLE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        PlayerInventory inventory = player.getInventory();
        inventory.insertStack(SLOT_INDEX, addedStack);
        Slot slot = new Slot(inventory, SLOT_INDEX, 0, 0);
        context.addInstantFinalTask(() -> {
            context.assertTrue(bundleStack.onStackClicked(slot, ClickType.RIGHT, player), "Expected right-clicking with Bundle to be successful");
            Assert.itemStackIsEmpty(slot.getStack());
            Assert.itemStackHasDataComponent(bundleStack, DataComponentTypes.BUNDLE_CONTENTS, bundleContents -> {
                Assert.itemStackIsOf(bundleContents.get(0), ItemKeys.BUNDLE);
            });
            context.assertEquals(
                TestUtil.getItemComponent(bundleStack, ItemComponentTypes.ITEM_HOLDER).occupancy(bundleStack),
                BundleContentsComponentAccessor.nestedBundleOccupancy(),
                "occupancy"
            );
        });
    }
}
