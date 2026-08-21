package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
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
    public void consumingHoneyBottleRemovesPoisonStatusEffect(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getFoodData().setFoodLevel(0);
        player.addEffect(new MobEffectInstance(MobEffects.POISON, MobEffectInstance.INFINITE_DURATION));
        ServerLevel level = helper.getLevel();
        ItemStack honeyBottle = level.itematic$createStack(ItemIds.HONEY_BOTTLE);
        player.setItemInHand(InteractionHand.MAIN_HAND, honeyBottle);
        level.addFreshEntity(player);
        helper.startSequence()
            .thenExecute(() -> honeyBottle.use(level, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(
                honeyBottle.getUseDuration(player) + 1,
                () -> Assert.livingEntity(helper, player)
                    .doesNotHaveEffect(MobEffects.POISON)
            )
            .thenSucceed();
    }
}
