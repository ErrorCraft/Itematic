package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class IronSwordTestSuite {
    @GameTest
    public void attackingEntityWhileInSurvivalModeDamagesIronSword(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.IRON_SWORD));
        PigEntity pig = TestUtil.createEntity(context, EntityType.PIG, entity -> {});
        context.addFinalTask(() -> {
            player.attack(pig);
            Assert.itemStack(context, player.getMainHandStack())
                .isDamaged();
        });
    }

    @GameTest
    @SuppressWarnings("removal")
    public void attackingEntityWhileInCreativeModeDoesNotDamageIronSword(TestContext context) {
        ServerPlayerEntity player = context.createMockCreativeServerPlayerInWorld();
        ServerWorld world = context.getWorld();
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.IRON_SWORD));
        PigEntity pig = TestUtil.createEntity(context, EntityType.PIG, entity -> {});
        context.addFinalTask(() -> {
            player.attack(pig);
            Assert.itemStack(context, player.getMainHandStack())
                .isNotDamaged();
        });
    }
}
