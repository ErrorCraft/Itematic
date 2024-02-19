package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class LifeSavingItemComponentTestSuite {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void dyingWhileHoldingTotemOfUndyingKeepsPlayerAlive(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.TOTEM_OF_UNDYING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        player.damage(world.getDamageSources().fall(), Float.MAX_VALUE);
        context.addInstantFinalTask(() -> {
            Assert.areFloatsEqual(player.getHealth(), 1.0f, (value, expected) -> "Expected health to be " + expected + ", got " + value + " instead");
            context.expectEntityHasEffect(player, StatusEffects.REGENERATION, 1);
            context.expectEntityHasEffect(player, StatusEffects.ABSORPTION, 1);
            context.expectEntityHasEffect(player, StatusEffects.FIRE_RESISTANCE, 0);
        });
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void dyingFromBypassedInvulnerabilityDamageWhileHoldingTotemOfUndyingKillsPlayer(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.TOTEM_OF_UNDYING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        player.damage(world.getDamageSources().genericKill(), Float.MAX_VALUE);
        context.addInstantFinalTask(() -> context.assertTrue(player.isDead(), "Expected player to be dead"));
    }
}
