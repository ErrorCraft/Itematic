package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class EquipmentItemComponentTestSuite {
    @GameTest
    public void usingItemEquipsStack(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack leatherHelmet = world.itematic$createStack(ItemKeys.LEATHER_HELMET);
        player.setStackInHand(Hand.MAIN_HAND, leatherHelmet);
        EquippableComponent equippable = TestUtil.getDataComponent(context, leatherHelmet, DataComponentTypes.EQUIPPABLE);
        leatherHelmet.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> Assert.itemStack(context, player.getEquippedStack(equippable.slot()))
            .is(ItemKeys.LEATHER_HELMET)
        );
    }

    @GameTest
    public void usingItemThatIsNotSwappableDoesNotEquipStack(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack shield = world.itematic$createStack(ItemKeys.SHIELD);
        player.setStackInHand(Hand.MAIN_HAND, shield);
        EquippableComponent equippable = TestUtil.getDataComponent(context, shield, DataComponentTypes.EQUIPPABLE);
        shield.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> Assert.itemStack(context, player.getEquippedStack(equippable.slot()))
            .isEmpty()
        );
    }

    @GameTest
    public void usingItemWithAlreadyEquippedStackSwapsStacks(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        player.equipStack(EquipmentSlot.HEAD, world.itematic$createStack(ItemKeys.IRON_HELMET));
        ItemStack leatherHelmet = world.itematic$createStack(ItemKeys.LEATHER_HELMET);
        player.setStackInHand(Hand.MAIN_HAND, leatherHelmet);
        context.addFinalTask(() -> {
            ActionResult result = leatherHelmet.use(world, player, Hand.MAIN_HAND);
            Assert.isInstance(
                context,
                result,
                ActionResult.Success.class,
                () -> "Expected equipment item usage to be successful",
                success -> Assert.itemStack(context, success.getNewHandStack())
                    .is(ItemKeys.IRON_HELMET)
            );
            Assert.itemStack(context, player.getEquippedStack(EquipmentSlot.HEAD))
                .is(ItemKeys.LEATHER_HELMET);
        });
    }
}
