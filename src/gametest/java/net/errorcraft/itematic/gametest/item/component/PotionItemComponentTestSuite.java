package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class PotionItemComponentTestSuite {
    @GameTest(maxTicks = 100)
    public void drinkingPotionItemAddsEffects(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack potion = PotionContentsComponentUtil.setPotion(
            world.itematic$createStack(ItemKeys.POTION),
            Potions.LEAPING
        );
        player.setStackInHand(Hand.MAIN_HAND, potion);
        world.spawnEntity(player);
        potion.use(world, player, Hand.MAIN_HAND);
        context.createTimedTaskRunner().expectMinDurationAndRun(
            potion.getMaxUseTime(player),
            () -> Assert.livingEntity(context, player)
                .hasEffects(Potions.LEAPING)
        ).completeIfSuccessful();
    }
}
