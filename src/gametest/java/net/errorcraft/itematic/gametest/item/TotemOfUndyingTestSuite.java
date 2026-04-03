package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.assertion.ItemStackAssert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class TotemOfUndyingTestSuite {
    @GameTest
    public void holdingTotemOfUndyingSavesHolderFromDeath(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack totemOfUndying = world.itematic$createStack(ItemKeys.TOTEM_OF_UNDYING);
        player.setStackInHand(Hand.MAIN_HAND, totemOfUndying);
        player.damage(world, world.getDamageSources().fall(), Float.MAX_VALUE);
        context.addFinalTask(() -> Assert.livingEntity(context, player)
            .hasHealth(health -> health.equals(1.0f))
            .hasEffect(StatusEffects.REGENERATION, 1)
            .hasEffect(StatusEffects.ABSORPTION, 1)
            .hasEffect(StatusEffects.FIRE_RESISTANCE, 0)
            .hasStackInHand(Hand.MAIN_HAND, ItemStackAssert::isEmpty));
    }
}
