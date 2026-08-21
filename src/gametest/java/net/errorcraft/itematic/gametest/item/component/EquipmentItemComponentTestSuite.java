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
    public void usingItemEquipsStack(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        ItemStack leatherHelmet = level.itematic$createStack(ItemIds.LEATHER_HELMET);
        player.setItemInHand(InteractionHand.MAIN_HAND, leatherHelmet);
        Equippable equippable = TestUtil.getDataComponent(helper, leatherHelmet, DataComponents.EQUIPPABLE);
        leatherHelmet.use(level, player, InteractionHand.MAIN_HAND);
        helper.succeedIf(() -> Assert.itemStack(helper, player.getItemBySlot(equippable.slot()))
            .is(ItemIds.LEATHER_HELMET)
        );
    }

    @GameTest
    public void usingItemThatIsNotSwappableDoesNotEquipStack(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        ItemStack shield = level.itematic$createStack(ItemIds.SHIELD);
        player.setItemInHand(InteractionHand.MAIN_HAND, shield);
        Equippable equippable = TestUtil.getDataComponent(helper, shield, DataComponents.EQUIPPABLE);
        shield.use(level, player, InteractionHand.MAIN_HAND);
        helper.succeedIf(() -> Assert.itemStack(helper, player.getItemBySlot(equippable.slot()))
            .isEmpty()
        );
    }

    @GameTest
    public void usingItemWithAlreadyEquippedStackSwapsStacks(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        player.setItemSlot(EquipmentSlot.HEAD, level.itematic$createStack(ItemIds.IRON_HELMET));
        ItemStack leatherHelmet = level.itematic$createStack(ItemIds.LEATHER_HELMET);
        player.setItemInHand(InteractionHand.MAIN_HAND, leatherHelmet);
        helper.succeedIf(() -> {
            InteractionResult result = leatherHelmet.use(level, player, InteractionHand.MAIN_HAND);
            Assert.interactionResult(helper, result, "equipment item usage")
                .resultStack(stack -> stack.is(ItemIds.IRON_HELMET));
            Assert.itemStack(helper, player.getItemBySlot(EquipmentSlot.HEAD))
                .is(ItemIds.LEATHER_HELMET);
        });
    }
}
