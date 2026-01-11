package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.inventory.StackReferenceUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.ClickType;
import net.minecraft.world.GameMode;

import java.util.Objects;

public class ItemHolderItemComponentTestSuite {
    private static final int SLOT = 0;

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void rightClickingOnStackWithItemHolderAddsStackToItemHolder(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        PlayerInventory inventory = player.getInventory();
        ItemStack stack = world.itematic$createStack(ItemKeys.BUNDLE);
        inventory.insertStack(SLOT, world.itematic$createStack(ItemKeys.STICK));
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        world.spawnEntity(player);
        boolean success = stack.onStackClicked(slot, ClickType.RIGHT, player);
        context.addInstantFinalTask(() -> {
            context.assertTrue(success, "Expected right clicking with item holder to be successful");
            Assert.itemStackIsEmpty(inventory.getStack(SLOT));
            Assert.itemStackHasComponent(stack, DataComponentTypes.BUNDLE_CONTENTS,
                component -> Assert.itemStackIsOf(component.get(0), ItemKeys.STICK)
            );
        });
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void rightClickingOnEmptySlotPlacesLastStackFromItemHolderInSlot(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        PlayerInventory inventory = player.getInventory();
        ItemStack stack = world.itematic$createStack(ItemKeys.BUNDLE);
        ItemStack stackToRemove = world.itematic$createStack(ItemKeys.STICK);
        addToBundleContentsComponent(stack, stackToRemove);
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        world.spawnEntity(player);
        boolean success = stack.onStackClicked(slot, ClickType.RIGHT, player);
        context.addInstantFinalTask(() -> {
            context.assertTrue(success, "Expected right clicking with item holder to be successful");
            Assert.itemStackIsOf(inventory.getStack(SLOT), ItemKeys.STICK);
            Assert.itemStackHasComponent(stack, DataComponentTypes.BUNDLE_CONTENTS,
                component -> context.assertTrue(component.isEmpty(), "Expected item holder to be empty")
            );
        });
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void rightClickingOnItemHolderWithStackAddsStackToItemHolder(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        PlayerInventory inventory = player.getInventory();
        inventory.insertStack(SLOT, world.itematic$createStack(ItemKeys.BUNDLE));
        ItemStack stack = inventory.getStack(SLOT);
        ItemStack stackToAdd = world.itematic$createStack(ItemKeys.STICK);
        StackReference cursorStack = StackReferenceUtil.of(stackToAdd);
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        world.spawnEntity(player);
        boolean success = stack.onClicked(stackToAdd, slot, ClickType.RIGHT, player, cursorStack);
        context.addInstantFinalTask(() -> {
            context.assertTrue(success, "Expected right clicking on item holder to be successful");
            Assert.itemStackIsEmpty(cursorStack.get());
            Assert.itemStackHasComponent(inventory.getStack(SLOT), DataComponentTypes.BUNDLE_CONTENTS,
                component -> Assert.itemStackIsOf(component.get(0), ItemKeys.STICK)
            );
        });
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void rightClickingOnItemHolderRemovesStackFromItemHolder(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        PlayerInventory inventory = player.getInventory();
        ItemStack stack = world.itematic$createStack(ItemKeys.BUNDLE);
        ItemStack stackToRemove = world.itematic$createStack(ItemKeys.STICK);
        addToBundleContentsComponent(stack, stackToRemove);
        inventory.insertStack(SLOT, stack);
        stack = inventory.getStack(SLOT);
        StackReference cursorStack = StackReferenceUtil.of(ItemStack.EMPTY);
        Slot slot = new Slot(inventory, SLOT, 0, 0);
        world.spawnEntity(player);
        boolean success = stack.onClicked(ItemStack.EMPTY, slot, ClickType.RIGHT, player, cursorStack);
        context.addInstantFinalTask(() -> {
            context.assertTrue(success, "Expected right clicking on item holder to be successful");
            Assert.itemStackIsOf(cursorStack.get(), ItemKeys.STICK);
            Assert.itemStackHasComponent(inventory.getStack(SLOT), DataComponentTypes.BUNDLE_CONTENTS,
                component -> context.assertTrue(component.isEmpty(), "Expected item holder to be empty")
            );
        });
    }

    private static void addToBundleContentsComponent(ItemStack origin, ItemStack stackToAdd) {
        BundleContentsComponent.Builder builder = Objects.requireNonNull(
            TestUtil.getItemComponent(origin, ItemComponentTypes.ITEM_HOLDER).createBuilder(origin)
        );
        builder.add(stackToAdd);
        origin.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
    }
}
