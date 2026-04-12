package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class FishingRodTestSuite {
    @GameTest
    public void usingFishingRodCastsFishingRod(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack fishingRod = world.itematic$createStack(ItemKeys.FISHING_ROD);
        player.setStackInHand(Hand.MAIN_HAND, fishingRod);
        fishingRod.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> Assert.isTrue(
            context,
            player.fishHook != null,
            () -> "Expected Player to have cast a Fishing Rod"
        ));
    }

    @GameTest
    public void usingCastFishingRodRetractsFishingRod(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack fishingRod = world.itematic$createStack(ItemKeys.FISHING_ROD);
        player.setStackInHand(Hand.MAIN_HAND, fishingRod);
        ProjectileEntity.spawn(new FishingBobberEntity(player, world, 0, 0), world, fishingRod);
        fishingRod.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> Assert.isTrue(
            context,
            player.fishHook == null,
            () -> "Expected Player to have retracted a Fishing Rod"
        ));
    }
}
