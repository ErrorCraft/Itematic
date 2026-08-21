package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
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
    public void usingFishingRodCastsFishingRod(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        ItemStack fishingRod = level.itematic$createStack(ItemIds.FISHING_ROD);
        player.setItemInHand(InteractionHand.MAIN_HAND, fishingRod);
        fishingRod.use(level, player, InteractionHand.MAIN_HAND);
        helper.succeedIf(() -> Assert.isTrue(
            helper,
            player.fishing != null,
            () -> "Expected Player to have cast a Fishing Rod"
        ));
    }

    @GameTest
    public void usingCastFishingRodRetractsFishingRod(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        ItemStack fishingRod = level.itematic$createStack(ItemIds.FISHING_ROD);
        player.setItemInHand(InteractionHand.MAIN_HAND, fishingRod);
        Projectile.spawnProjectile(new FishingHook(player, level, 0, 0), level, fishingRod);
        fishingRod.use(level, player, InteractionHand.MAIN_HAND);
        helper.succeedIf(() -> Assert.isTrue(
            helper,
            player.fishing == null,
            () -> "Expected Player to have retracted a Fishing Rod"
        ));
    }
}
