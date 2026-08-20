package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class ConsumableItemComponentTestSuite {
    @GameTest(maxTicks = 100)
    public void consumingHoneyBottleReplacesItemWithGlassBottle(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getFoodData().setFoodLevel(0);
        ServerLevel level = helper.getLevel();
        ItemStack honeyBottle = level.itematic$createStack(ItemIds.HONEY_BOTTLE);
        player.setItemInHand(InteractionHand.MAIN_HAND, honeyBottle);
        level.addFreshEntity(player);
        helper.startSequence()
            .thenExecute(() -> honeyBottle.use(level, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(
                honeyBottle.getUseDuration(player),
                () -> Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                    .is(ItemIds.GLASS_BOTTLE)
            )
            .thenSucceed();
    }
}
