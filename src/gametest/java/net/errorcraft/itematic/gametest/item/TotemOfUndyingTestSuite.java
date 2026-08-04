package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.assertion.ItemStackAssert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class TotemOfUndyingTestSuite {
    @GameTest
    public void holdingTotemOfUndyingSavesHolderFromDeath(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        ItemStack totemOfUndying = world.itematic$createStack(ItemKeys.TOTEM_OF_UNDYING);
        player.setItemInHand(InteractionHand.MAIN_HAND, totemOfUndying);
        player.hurtServer(world, world.damageSources().fall(), Float.MAX_VALUE);
        context.succeedIf(() -> Assert.livingEntity(context, player)
            .hasHealth(health -> health.equals(1.0f))
            .hasEffect(MobEffects.REGENERATION, 1)
            .hasEffect(MobEffects.ABSORPTION, 1)
            .hasEffect(MobEffects.FIRE_RESISTANCE, 0)
            .hasStackInHand(InteractionHand.MAIN_HAND, ItemStackAssert::isEmpty));
    }
}
