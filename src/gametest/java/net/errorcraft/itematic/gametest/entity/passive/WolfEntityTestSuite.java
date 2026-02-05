package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class WolfEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(templateName = "itematic:entity.platform")
    @SuppressWarnings("removal")
    public void holdingBoneTemptsWolf(TestContext context) {
        WolfEntity wolf = context.spawnEntity(EntityType.WOLF, SPAWN_POSITION);
        wolf.setTamed(true, true);
        ServerPlayerEntity player = context.createMockCreativeServerPlayerInWorld();
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BONE);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        context.addInstantFinalTask(() -> context.assertTrue(wolf.isBegging(), "Expected wolf to be begging"));
    }

    @GameTest(templateName = "itematic:entity.platform")
    @SuppressWarnings("removal")
    public void holdingMeatTemptsWolf(TestContext context) {
        WolfEntity wolf = context.spawnEntity(EntityType.WOLF, SPAWN_POSITION);
        wolf.setTamed(true, true);
        ServerPlayerEntity player = context.createMockCreativeServerPlayerInWorld();
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BEEF);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        context.addInstantFinalTask(() -> context.assertTrue(wolf.isBegging(), "Expected wolf to be begging"));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void feedingWolfMeatHealsWolf(TestContext context) {
        WolfEntity wolf = context.spawnEntity(EntityType.WOLF, SPAWN_POSITION);
        wolf.setTamed(true, true);
        context.setHealthLow(wolf);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BEEF);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        wolf.interactMob(player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> context.assertTrue(wolf.getHealth() > 0.25f, "Expected wolf to be healed"));
    }
}
