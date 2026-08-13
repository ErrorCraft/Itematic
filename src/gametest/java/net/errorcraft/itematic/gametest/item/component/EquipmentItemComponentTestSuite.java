package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.GameType;

public class EquipmentItemComponentTestSuite {
    @GameTest
    public void usingItemEquipsStack(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        ItemStack leatherHelmet = world.itematic$createStack(ItemIds.LEATHER_HELMET);
        player.setItemInHand(InteractionHand.MAIN_HAND, leatherHelmet);
        Equippable equippable = TestUtil.getDataComponent(context, leatherHelmet, DataComponents.EQUIPPABLE);
        leatherHelmet.use(world, player, InteractionHand.MAIN_HAND);
        context.succeedIf(() -> Assert.itemStack(context, player.getItemBySlot(equippable.slot()))
            .is(ItemIds.LEATHER_HELMET)
        );
    }

    @GameTest
    public void usingItemThatIsNotSwappableDoesNotEquipStack(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        ItemStack shield = world.itematic$createStack(ItemIds.SHIELD);
        player.setItemInHand(InteractionHand.MAIN_HAND, shield);
        Equippable equippable = TestUtil.getDataComponent(context, shield, DataComponents.EQUIPPABLE);
        shield.use(world, player, InteractionHand.MAIN_HAND);
        context.succeedIf(() -> Assert.itemStack(context, player.getItemBySlot(equippable.slot()))
            .isEmpty()
        );
    }

    @GameTest
    public void usingItemWithAlreadyEquippedStackSwapsStacks(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        player.setItemSlot(EquipmentSlot.HEAD, world.itematic$createStack(ItemIds.IRON_HELMET));
        ItemStack leatherHelmet = world.itematic$createStack(ItemIds.LEATHER_HELMET);
        player.setItemInHand(InteractionHand.MAIN_HAND, leatherHelmet);
        context.succeedIf(() -> {
            InteractionResult result = leatherHelmet.use(world, player, InteractionHand.MAIN_HAND);
            Assert.isInstance(
                context,
                result,
                InteractionResult.Success.class,
                () -> "Expected equipment item usage to be successful",
                success -> Assert.itemStack(context, success.heldItemTransformedTo())
                    .is(ItemIds.IRON_HELMET)
            );
            Assert.itemStack(context, player.getItemBySlot(EquipmentSlot.HEAD))
                .is(ItemIds.LEATHER_HELMET);
        });
    }
}
