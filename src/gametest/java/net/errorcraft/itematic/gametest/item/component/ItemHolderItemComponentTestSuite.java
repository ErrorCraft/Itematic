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
    public void rightClickingOnStackWithItemHolderAddsStackToItemHolder(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        Inventory inventory = player.getInventory();
        ItemStack bundle = world.itematic$createStack(ItemIds.BUNDLE);
        inventory.add(SLOT, world.itematic$createStack(ItemIds.STICK));
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        world.addFreshEntity(player);
        context.succeedIf(() -> {
            boolean success = bundle.overrideStackedOnOther(slot, ClickAction.SECONDARY, player);
            Assert.isTrue(
                context,
                success,
                () -> "Expected right clicking on slot with item holder to be successful"
            );
            Assert.itemStack(context, inventory.getItem(SLOT))
                .isEmpty();
            Assert.itemStack(context, bundle)
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    component -> Assert.itemStack(context, component.getItemUnsafe(0))
                        .is(ItemIds.STICK)
                );
        });
    }

    @GameTest
    public void rightClickingOnEmptySlotPlacesLastStackFromItemHolderInSlot(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        Inventory inventory = player.getInventory();
        ItemStack bundle = world.itematic$createStack(ItemIds.BUNDLE);
        addToBundleContentsComponent(context, bundle, world.itematic$createStack(ItemIds.STICK));
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        world.addFreshEntity(player);
        context.succeedIf(() -> {
            boolean success = bundle.overrideStackedOnOther(slot, ClickAction.SECONDARY, player);
            Assert.isTrue(
                context,
                success,
                () -> "Expected right clicking on slot with item holder to be successful"
            );
            Assert.itemStack(context, inventory.getItem(SLOT))
                .is(ItemIds.STICK);
            Assert.itemStack(context, bundle)
                .hasComponent(DataComponents.BUNDLE_CONTENTS, component -> Assert.isTrue(
                    context,
                    component.isEmpty(),
                    () -> "Expected item holder contents to be empty"
                ));
        });
    }

    @GameTest
    public void rightClickingOnItemHolderWithStackAddsStackToItemHolder(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = context.getLevel();
        Slot inventorySlot = new Slot(player.getInventory(), SLOT, 0, 0);
        inventorySlot.setByPlayer(level.itematic$createStack(ItemIds.BUNDLE));
        SlotAccess carriedSlot = Objects.requireNonNull(player.getSlot(Player.HELD_ITEM_SLOT));
        carriedSlot.set(level.itematic$createStack(ItemIds.STICK));
        context.succeedIf(() -> {
            boolean success = inventorySlot.getItem()
                .overrideOtherStackedOnMe(carriedSlot.get(), inventorySlot, ClickAction.SECONDARY, player, carriedSlot);
            Assert.isTrue(
                context,
                success,
                () -> "Expected right clicking on item holder to be successful"
            );
            Assert.itemStack(context, carriedSlot.get())
                .isEmpty();
            Assert.itemStack(context, player.getInventory().getItem(SLOT))
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    component -> Assert.itemStack(context, component.getItemUnsafe(0))
                        .is(ItemIds.STICK)
                );
        });
    }

    @GameTest
    public void rightClickingOnItemHolderWithNoStackRemovesStackFromItemHolder(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = context.getLevel();
        ItemStack bundle = level.itematic$createStack(ItemIds.BUNDLE);
        addToBundleContentsComponent(context, bundle, level.itematic$createStack(ItemIds.STICK));
        Slot inventorySlot = new Slot(player.getInventory(), SLOT, 0, 0);
        inventorySlot.setByPlayer(bundle);
        SlotAccess carriedSlot = Objects.requireNonNull(player.getSlot(Player.HELD_ITEM_SLOT));
        context.succeedIf(() -> {
            boolean success = inventorySlot.getItem()
                .overrideOtherStackedOnMe(carriedSlot.get(), inventorySlot, ClickAction.SECONDARY, player, carriedSlot);
            Assert.isTrue(
                context,
                success,
                () -> "Expected right clicking on item holder to be successful"
            );
            Assert.itemStack(context, carriedSlot.get())
                .is(ItemIds.STICK);
            Assert.itemStack(context, player.getInventory().getItem(SLOT))
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    component -> Assert.isTrue(
                        context,
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
