package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class WolfEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    @SuppressWarnings("removal")
    public void holdingBoneTemptsWolf(TestContext context) {
        WolfEntity wolf = context.spawnEntity(EntityType.WOLF, SPAWN_POSITION);
        wolf.setTamed(true, true);
        ServerPlayerEntity player = context.createMockCreativeServerPlayerInWorld();
        player.setStackInHand(Hand.MAIN_HAND, context.getWorld().itematic$createStack(ItemKeys.BONE));
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        context.addInstantFinalTask(() -> Assert.isTrue(
            context,
            wolf.isBegging(),
            () -> "Expected wolf to be begging"
        ));
    }

    @GameTest(structure = "itematic:entity.platform")
    @SuppressWarnings("removal")
    public void holdingMeatTemptsWolf(TestContext context) {
        WolfEntity wolf = context.spawnEntity(EntityType.WOLF, SPAWN_POSITION);
        wolf.setTamed(true, true);
        ServerPlayerEntity player = context.createMockCreativeServerPlayerInWorld();
        player.setStackInHand(Hand.MAIN_HAND, context.getWorld().itematic$createStack(ItemKeys.BEEF));
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        context.addInstantFinalTask(() -> Assert.isTrue(
            context,
            wolf.isBegging(),
            () -> "Expected wolf to be begging"
        ));
    }

    @GameTest(structure = "itematic:entity.platform")
    public void feedingWolfMeatHealsWolf(TestContext context) {
        WolfEntity wolf = context.spawnEntity(EntityType.WOLF, SPAWN_POSITION);
        wolf.setTamed(true, true);
        context.setHealthLow(wolf);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, context.getWorld().itematic$createStack(ItemKeys.BEEF));
        wolf.interactMob(player, Hand.MAIN_HAND);
        context.addFinalTask(() -> Assert.livingEntity(context, wolf)
            .hasHealth(health -> health.isGreaterThan(0.25f)));
    }
}
