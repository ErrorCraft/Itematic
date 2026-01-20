package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class BeetrootSoupTestSuite {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void eatingBeetrootSoupLeavesBowlAfterConsuming(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.getHungerManager().setFoodLevel(0);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BEETROOT_SOUP);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> stack.use(world, player, Hand.MAIN_HAND))
            .expectMinDurationAndRun(
                stack.itematic$useDuration(player),
                () -> context.assertTrue(player.getInventory().contains(s -> s.itematic$isOf(ItemKeys.BOWL)), "Expected Player to have a Bowl in their inventory")
            )
            .completeIfSuccessful();
    }
}
