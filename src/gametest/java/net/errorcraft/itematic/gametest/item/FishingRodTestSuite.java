package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class FishingRodTestSuite {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void usingFishingRodCastsFishingRod(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.FISHING_ROD);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> context.assertTrue(player.fishHook != null, "Expected player to have cast a fishing rod"));
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void usingCastFishingRodRetractsFishingRod(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.FISHING_ROD);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(new FishingBobberEntity(player, world, 0, 0));
        stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> context.assertTrue(player.fishHook == null, "Expected player to have retracted a fishing rod"));
    }
}
