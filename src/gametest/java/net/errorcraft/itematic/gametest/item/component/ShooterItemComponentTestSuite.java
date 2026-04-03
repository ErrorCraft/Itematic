package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class ShooterItemComponentTestSuite {
    @GameTest(maxTicks = 100)
    public void usingCrossbowChargesArrowFromInventory(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack crossbow = world.itematic$createStack(ItemKeys.CROSSBOW);
        ItemStack ammunition = world.itematic$createStack(ItemKeys.ARROW);
        player.setStackInHand(Hand.MAIN_HAND, crossbow);
        player.getInventory().insertStack(ammunition);
        world.spawnEntity(player);
        crossbow.use(world, player, Hand.MAIN_HAND);
        context.createTimedTaskRunner().expectMinDurationAndRun(
            crossbow.getMaxUseTime(player),
            () -> {
                crossbow.onStoppedUsing(world, player, player.getItemUseTimeLeft());
                Assert.itemStack(context, player.getStackInHand(Hand.MAIN_HAND))
                    .hasComponent(
                        DataComponentTypes.CHARGED_PROJECTILES,
                        chargedProjectiles -> Assert.isTrue(
                            context,
                            chargedProjectiles.itematic$contains(ItemKeys.ARROW),
                            () -> "Expected item stack to have an Arrow as a charged projectile"
                        )
                    );
                Assert.isFalse(
                    context,
                    player.getInventory().contains(s -> s.itematic$isOf(ItemKeys.ARROW)),
                    () -> "Expected Player not to have any Arrows in their inventory"
                );
            }
        ).completeIfSuccessful();
    }

    @GameTest(maxTicks = 100)
    public void usingCrossbowChargesFireworkRocketFromOffhand(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack crossbow = world.itematic$createStack(ItemKeys.CROSSBOW);
        ItemStack ammunition = world.itematic$createStack(ItemKeys.FIREWORK_ROCKET);
        player.setStackInHand(Hand.MAIN_HAND, crossbow);
        player.setStackInHand(Hand.OFF_HAND, ammunition);
        world.spawnEntity(player);
        crossbow.use(world, player, Hand.MAIN_HAND);
        context.createTimedTaskRunner().expectMinDurationAndRun(
            crossbow.getMaxUseTime(player),
            () -> {
                crossbow.onStoppedUsing(world, player, player.getItemUseTimeLeft());
                Assert.itemStack(context, player.getStackInHand(Hand.MAIN_HAND))
                    .hasComponent(
                        DataComponentTypes.CHARGED_PROJECTILES,
                        chargedProjectiles -> Assert.isTrue(
                            context,
                            chargedProjectiles.itematic$contains(ItemKeys.FIREWORK_ROCKET),
                            () -> "Expected item stack to have a Firework Rocket as a charged projectile"
                        )
                    );
                Assert.itemStack(context, player.getStackInHand(Hand.OFF_HAND))
                    .isEmpty();
            }
        ).completeIfSuccessful();
    }
}
