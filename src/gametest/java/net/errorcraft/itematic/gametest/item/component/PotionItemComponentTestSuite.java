package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.GameType;

public class PotionItemComponentTestSuite {
    @GameTest(maxTicks = 100)
    public void drinkingPotionItemAddsEffects(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        ItemStack potion = PotionContentsUtil.setPotion(
            world.itematic$createStack(ItemIds.POTION),
            Potions.LEAPING
        );
        player.setItemInHand(InteractionHand.MAIN_HAND, potion);
        world.addFreshEntity(player);
        potion.use(world, player, InteractionHand.MAIN_HAND);
        context.startSequence().thenExecuteAfter(
            potion.getUseDuration(player),
            () -> Assert.livingEntity(context, player)
                .hasEffects(Potions.LEAPING)
        ).thenSucceed();
    }
}
