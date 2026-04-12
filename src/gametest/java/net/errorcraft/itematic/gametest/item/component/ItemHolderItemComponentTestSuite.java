package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.inventory.SimpleStackReference;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.ClickType;
import net.minecraft.world.GameMode;

import java.util.Objects;

public class ItemHolderItemComponentTestSuite {
    private static final int SLOT = 0;

    @GameTest
    public void rightClickingOnStackWithItemHolderAddsStackToItemHolder(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        PlayerInventory inventory = player.getInventory();
        ItemStack bundle = world.itematic$createStack(ItemKeys.BUNDLE);
        inventory.insertStack(SLOT, world.itematic$createStack(ItemKeys.STICK));
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        world.spawnEntity(player);
        context.addFinalTask(() -> {
            boolean success = bundle.onStackClicked(slot, ClickType.RIGHT, player);
            Assert.isTrue(
                context,
                success,
                () -> "Expected right clicking on slot with item holder to be successful"
            );
            Assert.itemStack(context, inventory.getStack(SLOT))
                .isEmpty();
            Assert.itemStack(context, bundle)
                .hasComponent(
                    DataComponentTypes.BUNDLE_CONTENTS,
                    component -> Assert.itemStack(context, component.get(0))
                        .is(ItemKeys.STICK)
                );
        });
    }

    @GameTest
    public void rightClickingOnEmptySlotPlacesLastStackFromItemHolderInSlot(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        PlayerInventory inventory = player.getInventory();
        ItemStack bundle = world.itematic$createStack(ItemKeys.BUNDLE);
        addToBundleContentsComponent(context, bundle, world.itematic$createStack(ItemKeys.STICK));
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        world.spawnEntity(player);
        context.addFinalTask(() -> {
            boolean success = bundle.onStackClicked(slot, ClickType.RIGHT, player);
            Assert.isTrue(
                context,
                success,
                () -> "Expected right clicking on slot with item holder to be successful"
            );
            Assert.itemStack(context, inventory.getStack(SLOT))
                .is(ItemKeys.STICK);
            Assert.itemStack(context, bundle)
                .hasComponent(DataComponentTypes.BUNDLE_CONTENTS, component -> Assert.isTrue(
                    context,
                    component.isEmpty(),
                    () -> "Expected item holder contents to be empty"
                ));
        });
    }

    @GameTest
    public void rightClickingOnItemHolderWithStackAddsStackToItemHolder(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        PlayerInventory inventory = player.getInventory();
        inventory.insertStack(SLOT, world.itematic$createStack(ItemKeys.BUNDLE));
        ItemStack bundle = inventory.getStack(SLOT);
        ItemStack stackToAdd = world.itematic$createStack(ItemKeys.STICK);
        StackReference cursorStack = SimpleStackReference.of(stackToAdd);
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        world.spawnEntity(player);
        context.addFinalTask(() -> {
            boolean success = bundle.onClicked(stackToAdd, slot, ClickType.RIGHT, player, cursorStack);
            Assert.isTrue(
                context,
                success,
                () -> "Expected right clicking on item holder to be successful"
            );
            Assert.itemStack(context, cursorStack.get())
                .isEmpty();
            Assert.itemStack(context, inventory.getStack(SLOT))
                .hasComponent(
                    DataComponentTypes.BUNDLE_CONTENTS,
                    component -> Assert.itemStack(context, component.get(0))
                        .is(ItemKeys.STICK)
                );
        });
    }

    @GameTest
    public void rightClickingOnItemHolderRemovesStackFromItemHolder(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        PlayerInventory inventory = player.getInventory();
        inventory.insertStack(SLOT, world.itematic$createStack(ItemKeys.BUNDLE));
        ItemStack bundle = inventory.getStack(SLOT);
        addToBundleContentsComponent(context, bundle, world.itematic$createStack(ItemKeys.STICK));
        StackReference cursorStack = SimpleStackReference.of(ItemStack.EMPTY);
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        world.spawnEntity(player);
        context.addFinalTask(() -> {
            boolean success = bundle.onClicked(ItemStack.EMPTY, slot, ClickType.RIGHT, player, cursorStack);
            Assert.isTrue(
                context,
                success,
                () -> "Expected right clicking on item holder to be successful"
            );
            Assert.itemStack(context, cursorStack.get())
                .is(ItemKeys.STICK);
            Assert.itemStack(context, inventory.getStack(SLOT))
                .hasComponent(
                    DataComponentTypes.BUNDLE_CONTENTS,
                    component -> Assert.isTrue(
                        context,
                        component.isEmpty(),
                        () -> "Expected item holder to be empty"
                    )
                );
        });
    }

    private static void addToBundleContentsComponent(TestContext helper, ItemStack bundle, ItemStack stackToAdd) {
        BundleContentsComponent.Builder builder = Objects.requireNonNull(
            TestUtil.getItemBehavior(helper, bundle, ItemComponentTypes.ITEM_HOLDER).createBuilder(bundle)
        );
        builder.add(stackToAdd);
        bundle.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
    }
}
