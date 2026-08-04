package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.inventory.SimpleStackReference;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.util.TestUtil;
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
        ItemStack bundle = world.itematic$createStack(ItemKeys.BUNDLE);
        inventory.add(SLOT, world.itematic$createStack(ItemKeys.STICK));
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
                        .is(ItemKeys.STICK)
                );
        });
    }

    @GameTest
    public void rightClickingOnEmptySlotPlacesLastStackFromItemHolderInSlot(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        Inventory inventory = player.getInventory();
        ItemStack bundle = world.itematic$createStack(ItemKeys.BUNDLE);
        addToBundleContentsComponent(context, bundle, world.itematic$createStack(ItemKeys.STICK));
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
                .is(ItemKeys.STICK);
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
        ServerLevel world = context.getLevel();
        Inventory inventory = player.getInventory();
        inventory.add(SLOT, world.itematic$createStack(ItemKeys.BUNDLE));
        ItemStack bundle = inventory.getItem(SLOT);
        ItemStack stackToAdd = world.itematic$createStack(ItemKeys.STICK);
        SlotAccess cursorStack = SimpleStackReference.of(stackToAdd);
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        world.addFreshEntity(player);
        context.succeedIf(() -> {
            boolean success = bundle.overrideOtherStackedOnMe(stackToAdd, slot, ClickAction.SECONDARY, player, cursorStack);
            Assert.isTrue(
                context,
                success,
                () -> "Expected right clicking on item holder to be successful"
            );
            Assert.itemStack(context, cursorStack.get())
                .isEmpty();
            Assert.itemStack(context, inventory.getItem(SLOT))
                .hasComponent(
                    DataComponents.BUNDLE_CONTENTS,
                    component -> Assert.itemStack(context, component.getItemUnsafe(0))
                        .is(ItemKeys.STICK)
                );
        });
    }

    @GameTest
    public void rightClickingOnItemHolderRemovesStackFromItemHolder(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        Inventory inventory = player.getInventory();
        inventory.add(SLOT, world.itematic$createStack(ItemKeys.BUNDLE));
        ItemStack bundle = inventory.getItem(SLOT);
        addToBundleContentsComponent(context, bundle, world.itematic$createStack(ItemKeys.STICK));
        SlotAccess cursorStack = SimpleStackReference.of(ItemStack.EMPTY);
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        world.addFreshEntity(player);
        context.succeedIf(() -> {
            boolean success = bundle.overrideOtherStackedOnMe(ItemStack.EMPTY, slot, ClickAction.SECONDARY, player, cursorStack);
            Assert.isTrue(
                context,
                success,
                () -> "Expected right clicking on item holder to be successful"
            );
            Assert.itemStack(context, cursorStack.get())
                .is(ItemKeys.STICK);
            Assert.itemStack(context, inventory.getItem(SLOT))
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
            TestUtil.getItemBehavior(helper, bundle, ItemComponentTypes.ITEM_HOLDER).createBuilder(bundle)
        );
        builder.tryInsert(stackToAdd);
        bundle.set(DataComponents.BUNDLE_CONTENTS, builder.toImmutable());
    }
}
