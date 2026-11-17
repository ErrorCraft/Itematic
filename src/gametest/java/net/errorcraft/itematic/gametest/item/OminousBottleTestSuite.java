package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.minecraft.world.level.GameType;

public class OminousBottleTestSuite {
    private static final int BAD_OMEN_AMPLIFIER = 1;

    @GameTest(maxTicks = 100)
    public void consumingOminousBottleWithOminousBottleAmplifierAddsBadOmenStatusEffect(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        ItemStack ominousBottle = level.itematic$createStack(ItemIds.OMINOUS_BOTTLE);
        ominousBottle.set(
            DataComponents.OMINOUS_BOTTLE_AMPLIFIER,
            new OminousBottleAmplifier(BAD_OMEN_AMPLIFIER)
        );
        player.setItemInHand(InteractionHand.MAIN_HAND, ominousBottle);
        level.addFreshEntity(player);
        helper.startSequence()
            .thenExecute(() -> ominousBottle.use(level, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(
                ominousBottle.getUseDuration(player) + 1,
                () -> Assert.livingEntity(helper, player)
                    .hasEffect(MobEffects.BAD_OMEN, BAD_OMEN_AMPLIFIER)
            )
            .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void consumingOminousBottleWithoutOminousBottleAmplifierDoesNotAddBadOmenStatusEffect(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        ItemStack ominousBottle = level.itematic$createStack(ItemIds.OMINOUS_BOTTLE);
        player.setItemInHand(InteractionHand.MAIN_HAND, ominousBottle);
        level.addFreshEntity(player);
        helper.startSequence()
            .thenExecute(() -> ominousBottle.use(level, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(
                ominousBottle.getUseDuration(player) + 1,
                () -> Assert.livingEntity(helper, player)
                    .doesNotHaveEffect(MobEffects.BAD_OMEN)
            )
            .thenSucceed();
    }
}
