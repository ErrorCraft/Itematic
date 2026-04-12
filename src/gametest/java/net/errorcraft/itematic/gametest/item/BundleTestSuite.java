package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.component.type.BundleContentsComponentAccessor;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.ClickType;
import net.minecraft.world.GameMode;

public class BundleTestSuite {
    private static final int SLOT_INDEX = 0;

    @GameTest
    public void addingNormalItemToBundleAddsIt(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack bundleStack = world.itematic$createStack(ItemKeys.BUNDLE);
        ItemStack addedStack = world.itematic$createStack(ItemKeys.STICK);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        PlayerInventory inventory = player.getInventory();
        inventory.insertStack(SLOT_INDEX, addedStack);
        Slot slot = new Slot(inventory, SLOT_INDEX, 0, 0);
        context.addFinalTask(() -> {
            Assert.isTrue(
                context,
                bundleStack.onStackClicked(slot, ClickType.RIGHT, player),
                () -> "Expected right clicking on slot with Bundle to be successful"
            );
            Assert.itemStack(context, slot.getStack())
                .isEmpty();
            Assert.itemStack(context, bundleStack)
                .hasComponent(
                    DataComponentTypes.BUNDLE_CONTENTS,
                    bundleContents -> Assert.itemStack(context, bundleContents.get(0))
                        .is(ItemKeys.STICK)
                );
        });
    }

    @GameTest
    public void addingShulkerBoxToBundleRejectsIt(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack bundleStack = world.itematic$createStack(ItemKeys.BUNDLE);
        ItemStack addedStack = world.itematic$createStack(ItemKeys.SHULKER_BOX);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        PlayerInventory inventory = player.getInventory();
        inventory.insertStack(SLOT_INDEX, addedStack);
        Slot slot = new Slot(inventory, SLOT_INDEX, 0, 0);
        context.addFinalTask(() -> {
            Assert.isTrue(
                context,
                bundleStack.onStackClicked(slot, ClickType.RIGHT, player),
                () -> "Expected right clicking on slot with Bundle to be successful"
            );
            Assert.itemStack(context, slot.getStack())
                .isNotEmpty();
            Assert.itemStack(context, bundleStack)
                .hasComponent(
                    DataComponentTypes.BUNDLE_CONTENTS,
                    bundleContents -> Assert.isTrue(context, bundleContents.isEmpty(), () -> "Expected Bundle to be empty")
                );
        });
    }

    @GameTest
    public void addingBundleToBundleAddsItWithPenalty(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack bundle = world.itematic$createStack(ItemKeys.BUNDLE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        PlayerInventory inventory = player.getInventory();
        inventory.insertStack(SLOT_INDEX, world.itematic$createStack(ItemKeys.BUNDLE));
        Slot slot = new Slot(inventory, SLOT_INDEX, 0, 0);
        context.addFinalTask(() -> {
            Assert.isTrue(
                context,
                bundle.onStackClicked(slot, ClickType.RIGHT, player),
                () -> "Expected right clicking on slot with Bundle to be successful"
            );
            Assert.itemStack(context, slot.getStack())
                .isEmpty();
            Assert.itemStack(context, bundle)
                .hasComponent(
                    DataComponentTypes.BUNDLE_CONTENTS,
                    bundleContents -> Assert.itemStack(context, bundleContents.get(0))
                        .is(ItemKeys.BUNDLE)
                );
            Assert.areEqual(
                context,
                TestUtil.getItemBehavior(context, bundle, ItemComponentTypes.ITEM_HOLDER).occupancy(bundle),
                BundleContentsComponentAccessor.nestedBundleOccupancy(),
                "occupancy"
            );
        });
    }
}
