package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.GameType;

import java.util.List;

public class FoodItemComponentTestSuite {
    @GameTest(maxTicks = 100)
    public void eatingFoodItemAddsNutrition(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getFoodData().setFoodLevel(0);
        ServerLevel level = helper.getLevel();
        ItemStack apple = level.itematic$createStack(ItemIds.APPLE);
        FoodProperties food = TestUtil.getDataComponent(helper, apple, DataComponents.FOOD);
        player.setItemInHand(InteractionHand.MAIN_HAND, apple);
        level.addFreshEntity(player);
        helper.startSequence()
            .thenExecute(() -> apple.use(level, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(
                apple.getUseDuration(player),
                () -> {
                    Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                        .isEmpty();
                    Assert.ints(helper, player.getFoodData().getFoodLevel(), "nutrition")
                        .equals(food.nutrition());
                }
            )
            .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void eatingSuspiciousStewAddsSuspiciousStewEffects(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        ItemStack suspiciousStew = level.itematic$createStack(ItemIds.SUSPICIOUS_STEW);
        List<SuspiciousStewEffects.Entry> suspiciousStewEffects = List.of(
            new SuspiciousStewEffects.Entry(MobEffects.SPEED, 100)
        );
        suspiciousStew.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, new SuspiciousStewEffects(suspiciousStewEffects));
        player.setItemInHand(InteractionHand.MAIN_HAND, suspiciousStew);
        level.addFreshEntity(player);
        helper.startSequence()
            .thenExecute(() -> suspiciousStew.use(level, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(
                suspiciousStew.getUseDuration(player),
                () -> Assert.livingEntity(helper, player)
                    .hasEffects(suspiciousStewEffects)
                    .hasStackInHand(InteractionHand.MAIN_HAND, stack -> stack.is(ItemIds.BOWL))
            )
            .thenSucceed();
    }
}
