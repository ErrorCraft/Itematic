package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class HoneyBottleTestSuite {
    @GameTest(maxTicks = 100)
    public void consumingHoneyBottleRemovesPoisonStatusEffect(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.getHungerManager().setFoodLevel(0);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, StatusEffectInstance.INFINITE));
        ServerWorld world = context.getWorld();
        ItemStack honeyBottle = world.itematic$createStack(ItemKeys.HONEY_BOTTLE);
        player.setStackInHand(Hand.MAIN_HAND, honeyBottle);
        world.spawnEntity(player);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> honeyBottle.use(world, player, Hand.MAIN_HAND))
            .expectMinDurationAndRun(
                honeyBottle.getMaxUseTime(player) + 1,
                () -> Assert.livingEntity(context, player)
                    .doesNotHaveEffect(StatusEffects.POISON)
            )
            .completeIfSuccessful();
    }
}
