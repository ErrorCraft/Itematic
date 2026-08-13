package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class LifeSavingItemComponentTestSuite {
    @GameTest
    public void dyingWhileHoldingTotemOfUndyingKeepsPlayerAlive(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemIds.TOTEM_OF_UNDYING));
        player.hurtServer(world, world.damageSources().fall(), Float.MAX_VALUE);
        context.succeedIf(() -> Assert.livingEntity(context, player)
            .hasHealth(health -> health.equals(1.0f))
            .hasEffect(MobEffects.REGENERATION, 1)
            .hasEffect(MobEffects.ABSORPTION, 1)
            .hasEffect(MobEffects.FIRE_RESISTANCE, 0));
    }

    @GameTest
    public void dyingFromBypassedInvulnerabilityDamageWhileHoldingTotemOfUndyingKillsPlayer(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemIds.TOTEM_OF_UNDYING));
        player.hurtServer(world, world.damageSources().genericKill(), Float.MAX_VALUE);
        context.succeedIf(() -> Assert.isTrue(
            context,
            player.isDeadOrDying(),
            () -> "Expected Player to be dead"
        ));
    }
}
