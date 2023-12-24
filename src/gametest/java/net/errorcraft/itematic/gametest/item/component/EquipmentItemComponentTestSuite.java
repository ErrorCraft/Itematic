package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EquipmentItemComponent;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;

public class EquipmentItemComponentTestSuite {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void usingItemEquipsStack(TestContext context) {
        PlayerEntity player = context.createMockSurvivalPlayer();
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.LEATHER_HELMET);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        EquipmentItemComponent component = TestUtil.getItemComponent(stack, ItemComponentTypes.EQUIPMENT);
        stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(player.getEquippedStack(component.slot()), ItemKeys.LEATHER_HELMET));
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void usingItemThatIsNotSwappableDoesNotEquipStack(TestContext context) {
        PlayerEntity player = context.createMockSurvivalPlayer();
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SHIELD);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        EquipmentItemComponent component = TestUtil.getItemComponent(stack, ItemComponentTypes.EQUIPMENT);
        stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> Assert.itemStackIsEmpty(player.getEquippedStack(component.slot())));
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void usingItemWithAlreadyEquippedStackSwapsStacks(TestContext context) {
        PlayerEntity player = context.createMockSurvivalPlayer();
        ServerWorld world = context.getWorld();
        player.equipStack(EquipmentSlot.HEAD, world.itematic$createStack(ItemKeys.IRON_HELMET));
        ItemStack stack = world.itematic$createStack(ItemKeys.LEATHER_HELMET);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        ItemStack resultStack = stack.use(world, player, Hand.MAIN_HAND).getValue();
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getEquippedStack(EquipmentSlot.HEAD), ItemKeys.LEATHER_HELMET);
            Assert.itemStackIsOf(resultStack, ItemKeys.IRON_HELMET);
        });
    }
}
