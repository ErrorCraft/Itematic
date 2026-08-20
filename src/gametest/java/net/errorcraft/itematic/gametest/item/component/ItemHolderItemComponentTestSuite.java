package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.GameType;

import java.util.Objects;

public class ItemHolderItemComponentTestSuite {
    private static final int SLOT = 0;

    @GameTest
    public void rightClickingOnStackWithItemHolderAddsStackToItemHolder(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        Inventory inventory = player.getInventory();
        ItemStack bundle = level.itematic$createStack(ItemIds.BUNDLE);
        inventory.add(SLOT, level.itematic$createStack(ItemIds.STICK));
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        level.addFreshEntity(player);
        helper.succeedIf(() -> {
            boolean success = bundle.overrideStackedOnOther(slot, ClickAction.SECONDARY, player);
            Assert.isTrue(
                helper,
                success,
                () -> "Expected right clicking on slot with item holder to be successful"
            );
            Assert.itemStack(helper, inventory.getItem(SLOT))
                .isEmpty();
            Assert.itemStack(helper, bundle)
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    component -> Assert.itemStack(helper, component.getItemUnsafe(0))
                        .is(ItemIds.STICK)
                );
        });
    }

    @GameTest
    public void rightClickingOnEmptySlotPlacesLastStackFromItemHolderInSlot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        Inventory inventory = player.getInventory();
        ItemStack bundle = level.itematic$createStack(ItemIds.BUNDLE);
        addToBundleContentsComponent(helper, bundle, level.itematic$createStack(ItemIds.STICK));
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        level.addFreshEntity(player);
        helper.succeedIf(() -> {
            boolean success = bundle.overrideStackedOnOther(slot, ClickAction.SECONDARY, player);
            Assert.isTrue(
                helper,
                success,
                () -> "Expected right clicking on slot with item holder to be successful"
            );
            Assert.itemStack(helper, inventory.getItem(SLOT))
                .is(ItemIds.STICK);
            Assert.itemStack(helper, bundle)
                .hasComponent(DataComponents.BUNDLE_CONTENTS, component -> Assert.isTrue(
                    helper,
                    component.isEmpty(),
                    () -> "Expected item holder contents to be empty"
                ));
        });
    }

    @GameTest
    public void rightClickingOnItemHolderWithStackAddsStackToItemHolder(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        Slot inventorySlot = new Slot(player.getInventory(), SLOT, 0, 0);
        inventorySlot.setByPlayer(level.itematic$createStack(ItemIds.BUNDLE));
        SlotAccess carriedSlot = Objects.requireNonNull(player.getSlot(Player.HELD_ITEM_SLOT));
        carriedSlot.set(level.itematic$createStack(ItemIds.STICK));
        helper.succeedIf(() -> {
            boolean success = inventorySlot.getItem()
                .overrideOtherStackedOnMe(carriedSlot.get(), inventorySlot, ClickAction.SECONDARY, player, carriedSlot);
            Assert.isTrue(
                helper,
                success,
                () -> "Expected right clicking on item holder to be successful"
            );
            Assert.itemStack(helper, carriedSlot.get())
                .isEmpty();
            Assert.itemStack(helper, player.getInventory().getItem(SLOT))
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    component -> Assert.itemStack(helper, component.getItemUnsafe(0))
                        .is(ItemIds.STICK)
                );
        });
    }

    @GameTest
    public void rightClickingOnItemHolderWithNoStackRemovesStackFromItemHolder(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        ItemStack bundle = level.itematic$createStack(ItemIds.BUNDLE);
        addToBundleContentsComponent(helper, bundle, level.itematic$createStack(ItemIds.STICK));
        Slot inventorySlot = new Slot(player.getInventory(), SLOT, 0, 0);
        inventorySlot.setByPlayer(bundle);
        SlotAccess carriedSlot = Objects.requireNonNull(player.getSlot(Player.HELD_ITEM_SLOT));
        helper.succeedIf(() -> {
            boolean success = inventorySlot.getItem()
                .overrideOtherStackedOnMe(carriedSlot.get(), inventorySlot, ClickAction.SECONDARY, player, carriedSlot);
            Assert.isTrue(
                helper,
                success,
                () -> "Expected right clicking on item holder to be successful"
            );
            Assert.itemStack(helper, carriedSlot.get())
                .is(ItemIds.STICK);
            Assert.itemStack(helper, player.getInventory().getItem(SLOT))
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    component -> Assert.isTrue(
                        helper,
                        component.isEmpty(),
                        () -> "Expected item holder to be empty"
                    )
                );
        });
    }

    private static void addToBundleContentsComponent(GameTestHelper helper, ItemStack bundle, ItemStack stackToAdd) {
        BundleContents.Mutable builder = Objects.requireNonNull(
            TestUtil.getItemBehavior(helper, bundle, ItemBehaviorType.ITEM_HOLDER).createBuilder(bundle)
        );
        builder.tryInsert(stackToAdd);
        bundle.set(DataComponents.BUNDLE_CONTENTS, builder.toImmutable());
    }
}
