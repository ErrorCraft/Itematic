package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class BeetrootSoupTestSuite {
    @GameTest(maxTicks = 100)
    public void eatingBeetrootSoupLeavesBowlAfterConsuming(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getFoodData().setFoodLevel(0);
        ServerLevel level = helper.getLevel();
        ItemStack beetrootSoup = level.itematic$createStack(ItemIds.BEETROOT_SOUP);
        player.setItemInHand(InteractionHand.MAIN_HAND, beetrootSoup);
        level.addFreshEntity(player);
        helper.startSequence()
            .thenExecute(() -> beetrootSoup.use(level, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(
                beetrootSoup.getUseDuration(player),
                () -> Assert.isTrue(
                    helper,
                    player.getInventory().contains(stack -> stack.is(ItemIds.BOWL)),
                    () -> "Expected Player to have a Bowl in their inventory"
                )
            )
            .thenSucceed();
    }
}
