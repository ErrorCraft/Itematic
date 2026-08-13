package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class ShooterItemComponentTestSuite {
    @GameTest(maxTicks = 100)
    public void usingCrossbowChargesArrowFromInventory(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        ItemStack crossbow = world.itematic$createStack(ItemIds.CROSSBOW);
        ItemStack ammunition = world.itematic$createStack(ItemIds.ARROW);
        player.setItemInHand(InteractionHand.MAIN_HAND, crossbow);
        player.getInventory().add(ammunition);
        world.addFreshEntity(player);
        crossbow.use(world, player, InteractionHand.MAIN_HAND);
        context.startSequence().thenExecuteAfter(
            crossbow.getUseDuration(player),
            () -> {
                crossbow.releaseUsing(world, player, player.getUseItemRemainingTicks());
                Assert.itemStack(context, player.getItemInHand(InteractionHand.MAIN_HAND))
                    .hasComponent(
                        DataComponents.CHARGED_PROJECTILES,
                        chargedProjectiles -> Assert.isTrue(
                            context,
                            chargedProjectiles.itematic$contains(ItemIds.ARROW),
                            () -> "Expected item stack to have an Arrow as a charged projectile"
                        )
                    );
                Assert.isFalse(
                    context,
                    player.getInventory().contains(s -> s.itematic$isOf(ItemIds.ARROW)),
                    () -> "Expected Player not to have any Arrows in their inventory"
                );
            }
        ).thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void usingCrossbowChargesFireworkRocketFromOffhand(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        ItemStack crossbow = world.itematic$createStack(ItemIds.CROSSBOW);
        ItemStack ammunition = world.itematic$createStack(ItemIds.FIREWORK_ROCKET);
        player.setItemInHand(InteractionHand.MAIN_HAND, crossbow);
        player.setItemInHand(InteractionHand.OFF_HAND, ammunition);
        world.addFreshEntity(player);
        crossbow.use(world, player, InteractionHand.MAIN_HAND);
        context.startSequence().thenExecuteAfter(
            crossbow.getUseDuration(player),
            () -> {
                crossbow.releaseUsing(world, player, player.getUseItemRemainingTicks());
                Assert.itemStack(context, player.getItemInHand(InteractionHand.MAIN_HAND))
                    .hasComponent(
                        DataComponents.CHARGED_PROJECTILES,
                        chargedProjectiles -> Assert.isTrue(
                            context,
                            chargedProjectiles.itematic$contains(ItemIds.FIREWORK_ROCKET),
                            () -> "Expected item stack to have a Firework Rocket as a charged projectile"
                        )
                    );
                Assert.itemStack(context, player.getItemInHand(InteractionHand.OFF_HAND))
                    .isEmpty();
            }
        ).thenSucceed();
    }
}
