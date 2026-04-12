package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class LifeSavingItemComponentTestSuite {
    @GameTest
    public void dyingWhileHoldingTotemOfUndyingKeepsPlayerAlive(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.TOTEM_OF_UNDYING));
        player.damage(world, world.getDamageSources().fall(), Float.MAX_VALUE);
        context.addFinalTask(() -> Assert.livingEntity(context, player)
            .hasHealth(health -> health.equals(1.0f))
            .hasEffect(StatusEffects.REGENERATION, 1)
            .hasEffect(StatusEffects.ABSORPTION, 1)
            .hasEffect(StatusEffects.FIRE_RESISTANCE, 0));
    }

    @GameTest
    public void dyingFromBypassedInvulnerabilityDamageWhileHoldingTotemOfUndyingKillsPlayer(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.TOTEM_OF_UNDYING));
        player.damage(world, world.getDamageSources().genericKill(), Float.MAX_VALUE);
        context.addFinalTask(() -> Assert.isTrue(
            context,
            player.isDead(),
            () -> "Expected Player to be dead"
        ));
    }
}
