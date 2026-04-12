package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class ConsumableItemComponentTestSuite {
    @GameTest(maxTicks = 100)
    public void consumingHoneyBottleReplacesItemWithGlassBottle(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.getHungerManager().setFoodLevel(0);
        ServerWorld world = context.getWorld();
        ItemStack honeyBottle = world.itematic$createStack(ItemKeys.HONEY_BOTTLE);
        player.setStackInHand(Hand.MAIN_HAND, honeyBottle);
        world.spawnEntity(player);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> honeyBottle.use(world, player, Hand.MAIN_HAND))
            .expectMinDurationAndRun(
                honeyBottle.getMaxUseTime(player),
                () -> Assert.itemStack(context, player.getStackInHand(Hand.MAIN_HAND))
                    .is(ItemKeys.GLASS_BOTTLE)
            )
            .completeIfSuccessful();
    }
}
