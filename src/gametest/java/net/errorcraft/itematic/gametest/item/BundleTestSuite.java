package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.mixin.world.item.component.BundleContentsAccessor;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class BundleTestSuite {
    private static final int SLOT_INDEX = 0;

    @GameTest
    public void addingNormalItemToBundleAddsIt(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack bundleStack = level.itematic$createStack(ItemIds.BUNDLE);
        ItemStack addedStack = level.itematic$createStack(ItemIds.STICK);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Inventory inventory = player.getInventory();
        inventory.add(SLOT_INDEX, addedStack);
        Slot slot = new Slot(inventory, SLOT_INDEX, 0, 0);
        helper.succeedIf(() -> {
            Assert.isTrue(
                helper,
                bundleStack.overrideStackedOnOther(slot, ClickAction.SECONDARY, player),
                () -> "Expected right clicking on slot with Bundle to be successful"
            );
            Assert.itemStack(helper, slot.getItem())
                .isEmpty();
            Assert.itemStack(helper, bundleStack)
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    bundleContents -> Assert.itemStack(helper, bundleContents.getItemUnsafe(0))
                        .is(ItemIds.STICK)
                );
        });
    }

    @GameTest
    public void addingShulkerBoxToBundleRejectsIt(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack bundleStack = level.itematic$createStack(ItemIds.BUNDLE);
        ItemStack addedStack = level.itematic$createStack(ItemIds.SHULKER_BOX);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Inventory inventory = player.getInventory();
        inventory.add(SLOT_INDEX, addedStack);
        Slot slot = new Slot(inventory, SLOT_INDEX, 0, 0);
        helper.succeedIf(() -> {
            Assert.isTrue(
                helper,
                bundleStack.overrideStackedOnOther(slot, ClickAction.SECONDARY, player),
                () -> "Expected right clicking on slot with Bundle to be successful"
            );
            Assert.itemStack(helper, slot.getItem())
                .isNotEmpty();
            Assert.itemStack(helper, bundleStack)
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    bundleContents -> Assert.isTrue(helper, bundleContents.isEmpty(), () -> "Expected Bundle to be empty")
                );
        });
    }

    @GameTest
    public void addingBundleToBundleAddsItWithPenalty(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack bundle = level.itematic$createStack(ItemIds.BUNDLE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Inventory inventory = player.getInventory();
        inventory.add(SLOT_INDEX, level.itematic$createStack(ItemIds.BUNDLE));
        Slot slot = new Slot(inventory, SLOT_INDEX, 0, 0);
        helper.succeedIf(() -> {
            Assert.isTrue(
                helper,
                bundle.overrideStackedOnOther(slot, ClickAction.SECONDARY, player),
                () -> "Expected right clicking on slot with Bundle to be successful"
            );
            Assert.itemStack(helper, slot.getItem())
                .isEmpty();
            Assert.itemStack(helper, bundle)
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    bundleContents -> Assert.itemStack(helper, bundleContents.getItemUnsafe(0))
                        .is(ItemIds.BUNDLE)
                );
            Assert.areEqual(
                helper,
                TestUtil.getItemBehavior(helper, bundle, ItemBehaviorType.ITEM_HOLDER).occupancy(bundle),
                BundleContentsAccessor.nestedBundleOccupancy(),
                "occupancy"
            );
        });
    }
}
