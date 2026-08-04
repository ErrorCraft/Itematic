package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class FishingRodTestSuite {
    @GameTest
    public void usingFishingRodCastsFishingRod(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        ItemStack fishingRod = world.itematic$createStack(ItemKeys.FISHING_ROD);
        player.setItemInHand(InteractionHand.MAIN_HAND, fishingRod);
        fishingRod.use(world, player, InteractionHand.MAIN_HAND);
        context.succeedIf(() -> Assert.isTrue(
            context,
            player.fishing != null,
            () -> "Expected Player to have cast a Fishing Rod"
        ));
    }

    @GameTest
    public void usingCastFishingRodRetractsFishingRod(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        ItemStack fishingRod = world.itematic$createStack(ItemKeys.FISHING_ROD);
        player.setItemInHand(InteractionHand.MAIN_HAND, fishingRod);
        Projectile.spawnProjectile(new FishingHook(player, world, 0, 0), world, fishingRod);
        fishingRod.use(world, player, InteractionHand.MAIN_HAND);
        context.succeedIf(() -> Assert.isTrue(
            context,
            player.fishing == null,
            () -> "Expected Player to have retracted a Fishing Rod"
        ));
    }
}
