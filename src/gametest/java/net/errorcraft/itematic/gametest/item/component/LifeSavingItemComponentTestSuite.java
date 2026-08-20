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
    public void dyingWhileHoldingTotemOfUndyingKeepsPlayerAlive(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.TOTEM_OF_UNDYING)
        );
        player.hurtServer(level, level.damageSources().fall(), Float.MAX_VALUE);
        helper.succeedIf(() -> Assert.livingEntity(helper, player)
            .hasHealth(health -> health.equals(1.0f))
            .hasEffect(MobEffects.REGENERATION, 1)
            .hasEffect(MobEffects.ABSORPTION, 1)
            .hasEffect(MobEffects.FIRE_RESISTANCE, 0));
    }

    @GameTest
    public void dyingFromBypassedInvulnerabilityDamageWhileHoldingTotemOfUndyingKillsPlayer(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.TOTEM_OF_UNDYING)
        );
        player.hurtServer(level, level.damageSources().genericKill(), Float.MAX_VALUE);
        helper.succeedIf(() -> Assert.isTrue(
            helper,
            player.isDeadOrDying(),
            () -> "Expected Player to be dead"
        ));
    }
}
