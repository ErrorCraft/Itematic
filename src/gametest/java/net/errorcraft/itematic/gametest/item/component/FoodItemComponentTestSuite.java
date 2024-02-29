package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.FoodItemComponent;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class FoodItemComponentTestSuite {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void eatingFoodItemAddsNutrition(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.getHungerManager().setFoodLevel(0);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.APPLE);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        FoodItemComponent component = TestUtil.getItemComponent(stack, ItemComponentTypes.FOOD);
        stack.use(world, player, Hand.MAIN_HAND);
        context.createTimedTaskRunner().expectMinDurationAndRun(
            TestUtil.getItemComponent(stack, ItemComponentTypes.USE_DURATION).ticks(),
            () -> {
                Assert.itemStackIsEmpty(player.getStackInHand(Hand.MAIN_HAND));
                Assert.areIntsEqual(
                    player.getHungerManager().getFoodLevel(),
                    component.nutrition(),
                    (value, expected) -> "Expected nutrition to be " + expected + ", got " + value + " instead"
                );
            }
        ).completeIfSuccessful();
    }
}
