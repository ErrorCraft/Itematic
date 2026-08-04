package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class HoneyBottleTestSuite {
    @GameTest(maxTicks = 100)
    public void consumingHoneyBottleRemovesPoisonStatusEffect(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.getFoodData().setFoodLevel(0);
        player.addEffect(new MobEffectInstance(MobEffects.POISON, MobEffectInstance.INFINITE_DURATION));
        ServerLevel world = context.getLevel();
        ItemStack honeyBottle = world.itematic$createStack(ItemKeys.HONEY_BOTTLE);
        player.setItemInHand(InteractionHand.MAIN_HAND, honeyBottle);
        world.addFreshEntity(player);
        context.startSequence()
            .thenExecute(() -> honeyBottle.use(world, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(
                honeyBottle.getUseDuration(player) + 1,
                () -> Assert.livingEntity(context, player)
                    .doesNotHaveEffect(MobEffects.POISON)
            )
            .thenSucceed();
    }
}
