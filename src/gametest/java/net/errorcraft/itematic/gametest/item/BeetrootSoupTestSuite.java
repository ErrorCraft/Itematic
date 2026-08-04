package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class BeetrootSoupTestSuite {
    @GameTest(maxTicks = 100)
    public void eatingBeetrootSoupLeavesBowlAfterConsuming(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.getFoodData().setFoodLevel(0);
        ServerLevel world = context.getLevel();
        ItemStack beetrootSoup = world.itematic$createStack(ItemKeys.BEETROOT_SOUP);
        player.setItemInHand(InteractionHand.MAIN_HAND, beetrootSoup);
        world.addFreshEntity(player);
        context.startSequence()
            .thenExecute(() -> beetrootSoup.use(world, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(
                beetrootSoup.getUseDuration(player),
                () -> Assert.isTrue(
                    context,
                    player.getInventory().contains(stack -> stack.itematic$isOf(ItemKeys.BOWL)),
                    () -> "Expected Player to have a Bowl in their inventory"
                )
            )
            .thenSucceed();
    }
}
