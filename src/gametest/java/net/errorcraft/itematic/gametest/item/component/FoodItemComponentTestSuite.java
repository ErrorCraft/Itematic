package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.GameType;

import java.util.List;

public class FoodItemComponentTestSuite {
    @GameTest(maxTicks = 100)
    public void eatingFoodItemAddsNutrition(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack apple = world.itematic$createStack(ItemIds.APPLE);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.getFoodData().setFoodLevel(0);
        player.setItemInHand(InteractionHand.MAIN_HAND, apple);
        world.addFreshEntity(player);
        FoodProperties food = TestUtil.getDataComponent(context, apple, DataComponents.FOOD);
        apple.use(world, player, InteractionHand.MAIN_HAND);
        context.startSequence().thenExecuteAfter(
            apple.getUseDuration(player),
            () -> {
                Assert.itemStack(context, player.getItemInHand(InteractionHand.MAIN_HAND))
                    .isEmpty();
                Assert.ints(context, player.getFoodData().getFoodLevel(), "nutrition")
                    .equals(food.nutrition());
            }
        ).thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void eatingSuspiciousStewAddsSuspiciousEffects(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack suspiciousStew = world.itematic$createStack(ItemIds.SUSPICIOUS_STEW);
        List<SuspiciousStewEffects.Entry> effects = TestUtil.getItemBehavior(context, world.itematic$createStack(ItemIds.DANDELION), ItemBehaviorType.SUSPICIOUS_EFFECT_INGREDIENT)
            .effects();
        suspiciousStew.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, new SuspiciousStewEffects(effects));
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, suspiciousStew);
        world.addFreshEntity(player);
        suspiciousStew.use(world, player, InteractionHand.MAIN_HAND);
        context.startSequence().thenExecuteAfter(
            suspiciousStew.getUseDuration(player),
            () -> Assert.livingEntity(context, player)
                .hasEffects(effects)
                .hasStackInHand(InteractionHand.MAIN_HAND, stack -> stack.is(ItemIds.BOWL))
        ).thenSucceed();
    }
}
