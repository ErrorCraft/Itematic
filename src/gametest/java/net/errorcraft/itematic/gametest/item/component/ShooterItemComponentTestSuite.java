package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class ShooterItemComponentTestSuite {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void usingCrossbowLoadsArrowFromInventory(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CROSSBOW);
        ItemStack ammunition = world.itematic$createStack(ItemKeys.ARROW);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        player.getInventory().insertStack(ammunition);
        world.spawnEntity(player);
        stack.use(world, player, Hand.MAIN_HAND);
        context.createTimedTaskRunner().expectMinDurationAndRun(
            ShooterItemComponent.getPullTime(stack),
            () -> {
                stack.onStoppedUsing(world, player, player.getItemUseTimeLeft());
                Assert.itemStackHasComponent(player.getStackInHand(Hand.MAIN_HAND), DataComponentTypes.CHARGED_PROJECTILES,
                    component -> context.assertTrue(component.itematic$contains(ItemKeys.ARROW), "Expected item stack to have an arrow as a charged projectile")
                );
                context.assertTrue(!player.getInventory().contains(s -> s.itematic$isOf(ItemKeys.ARROW)), "Expected player to have no arrows in their inventory");
            }
        ).completeIfSuccessful();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void usingCrossbowLoadsFireworkRocketFromOffhand(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CROSSBOW);
        ItemStack ammunition = world.itematic$createStack(ItemKeys.FIREWORK_ROCKET);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        player.setStackInHand(Hand.OFF_HAND, ammunition);
        world.spawnEntity(player);
        stack.use(world, player, Hand.MAIN_HAND);
        context.createTimedTaskRunner().expectMinDurationAndRun(
            ShooterItemComponent.getPullTime(stack),
            () -> {
                stack.onStoppedUsing(world, player, player.getItemUseTimeLeft());
                Assert.itemStackHasComponent(player.getStackInHand(Hand.MAIN_HAND), DataComponentTypes.CHARGED_PROJECTILES,
                    component -> context.assertTrue(component.itematic$contains(ItemKeys.FIREWORK_ROCKET), "Expected item stack to have a firework rocket as a charged projectile")
                );
                Assert.itemStackIsEmpty(player.getStackInHand(Hand.OFF_HAND));
            }
        ).completeIfSuccessful();
    }
}
