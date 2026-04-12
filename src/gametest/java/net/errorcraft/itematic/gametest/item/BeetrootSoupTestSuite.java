package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class BeetrootSoupTestSuite {
    @GameTest(maxTicks = 100)
    public void eatingBeetrootSoupLeavesBowlAfterConsuming(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.getHungerManager().setFoodLevel(0);
        ServerWorld world = context.getWorld();
        ItemStack beetrootSoup = world.itematic$createStack(ItemKeys.BEETROOT_SOUP);
        player.setStackInHand(Hand.MAIN_HAND, beetrootSoup);
        world.spawnEntity(player);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> beetrootSoup.use(world, player, Hand.MAIN_HAND))
            .expectMinDurationAndRun(
                beetrootSoup.getMaxUseTime(player),
                () -> Assert.isTrue(
                    context,
                    player.getInventory().contains(stack -> stack.itematic$isOf(ItemKeys.BOWL)),
                    () -> "Expected Player to have a Bowl in their inventory"
                )
            )
            .completeIfSuccessful();
    }
}
