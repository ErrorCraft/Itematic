package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.component.type.BundleContentsComponentAccessor;
import net.errorcraft.itematic.util.TestUtil;
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
    public void addingNormalItemToBundleAddsIt(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack bundleStack = world.itematic$createStack(ItemKeys.BUNDLE);
        ItemStack addedStack = world.itematic$createStack(ItemKeys.STICK);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        Inventory inventory = player.getInventory();
        inventory.add(SLOT_INDEX, addedStack);
        Slot slot = new Slot(inventory, SLOT_INDEX, 0, 0);
        context.succeedIf(() -> {
            Assert.isTrue(
                context,
                bundleStack.overrideStackedOnOther(slot, ClickAction.SECONDARY, player),
                () -> "Expected right clicking on slot with Bundle to be successful"
            );
            Assert.itemStack(context, slot.getItem())
                .isEmpty();
            Assert.itemStack(context, bundleStack)
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    bundleContents -> Assert.itemStack(context, bundleContents.getItemUnsafe(0))
                        .is(ItemKeys.STICK)
                );
        });
    }

    @GameTest
    public void addingShulkerBoxToBundleRejectsIt(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack bundleStack = world.itematic$createStack(ItemKeys.BUNDLE);
        ItemStack addedStack = world.itematic$createStack(ItemKeys.SHULKER_BOX);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        Inventory inventory = player.getInventory();
        inventory.add(SLOT_INDEX, addedStack);
        Slot slot = new Slot(inventory, SLOT_INDEX, 0, 0);
        context.succeedIf(() -> {
            Assert.isTrue(
                context,
                bundleStack.overrideStackedOnOther(slot, ClickAction.SECONDARY, player),
                () -> "Expected right clicking on slot with Bundle to be successful"
            );
            Assert.itemStack(context, slot.getItem())
                .isNotEmpty();
            Assert.itemStack(context, bundleStack)
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    bundleContents -> Assert.isTrue(context, bundleContents.isEmpty(), () -> "Expected Bundle to be empty")
                );
        });
    }

    @GameTest
    public void addingBundleToBundleAddsItWithPenalty(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack bundle = world.itematic$createStack(ItemKeys.BUNDLE);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        Inventory inventory = player.getInventory();
        inventory.add(SLOT_INDEX, world.itematic$createStack(ItemKeys.BUNDLE));
        Slot slot = new Slot(inventory, SLOT_INDEX, 0, 0);
        context.succeedIf(() -> {
            Assert.isTrue(
                context,
                bundle.overrideStackedOnOther(slot, ClickAction.SECONDARY, player),
                () -> "Expected right clicking on slot with Bundle to be successful"
            );
            Assert.itemStack(context, slot.getItem())
                .isEmpty();
            Assert.itemStack(context, bundle)
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    bundleContents -> Assert.itemStack(context, bundleContents.getItemUnsafe(0))
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
