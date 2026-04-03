package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.FoodItemComponent;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

import java.util.List;

public class FoodItemComponentTestSuite {
    @GameTest(maxTicks = 100)
    public void eatingFoodItemAddsNutrition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack apple = world.itematic$createStack(ItemKeys.APPLE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.getHungerManager().setFoodLevel(0);
        player.setStackInHand(Hand.MAIN_HAND, apple);
        world.spawnEntity(player);
        FoodItemComponent component = TestUtil.getItemBehavior(context, apple, ItemComponentTypes.FOOD);
        apple.use(world, player, Hand.MAIN_HAND);
        context.createTimedTaskRunner().expectMinDurationAndRun(
            apple.getMaxUseTime(player),
            () -> {
                Assert.itemStack(context, player.getStackInHand(Hand.MAIN_HAND))
                    .isEmpty();
                Assert.ints(context, player.getHungerManager().getFoodLevel(), "nutrition")
                    .equals(component.nutrition());
            }
        ).completeIfSuccessful();
    }

    @GameTest(maxTicks = 100)
    public void eatingSuspiciousStewAddsSuspiciousEffects(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack suspiciousStew = world.itematic$createStack(ItemKeys.SUSPICIOUS_STEW);
        List<SuspiciousStewEffectsComponent.StewEffect> effects = TestUtil.getItemBehavior(context, world.itematic$createStack(ItemKeys.DANDELION), ItemComponentTypes.SUSPICIOUS_EFFECT_INGREDIENT)
            .effects();
        suspiciousStew.set(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, new SuspiciousStewEffectsComponent(effects));
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, suspiciousStew);
        world.spawnEntity(player);
        suspiciousStew.use(world, player, Hand.MAIN_HAND);
        context.createTimedTaskRunner().expectMinDurationAndRun(
            suspiciousStew.getMaxUseTime(player),
            () -> Assert.livingEntity(context, player)
                .hasEffects(effects)
                .hasStackInHand(Hand.MAIN_HAND, stack -> stack.is(ItemKeys.BOWL))
        ).completeIfSuccessful();
    }
}
