package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class CrossbowTestSuite {
    @GameTest(maxTicks = 100)
    public void usingCrossbowWithInfinityChargesArrowFromInventoryButDoesNotConsumeTheArrow(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack crossbow = TestUtil.createItemStackWithEnchantment(world, ItemKeys.CROSSBOW, Enchantments.INFINITY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, crossbow);
        player.getInventory().insertStack(world.itematic$createStack(ItemKeys.ARROW));
        world.spawnEntity(player);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> crossbow.use(world, player, Hand.MAIN_HAND))
            .expectMinDurationAndRun(crossbow.getMaxUseTime(player), () -> {
                player.stopUsingItem();
                Assert.itemStack(context, player.getStackInHand(Hand.MAIN_HAND))
                    .hasComponent(
                        DataComponentTypes.CHARGED_PROJECTILES,
                        component -> Assert.isTrue(
                            context,
                            component.itematic$contains(ItemKeys.ARROW),
                            () -> "Expected item stack to have an Arrow as a charged projectile"
                        )
                    );
                Assert.isTrue(
                    context,
                    player.getInventory().contains(s -> s.itematic$isOf(ItemKeys.ARROW)),
                    () -> "Expected Player to have an Arrow in their inventory"
                );
            })
            .completeIfSuccessful();
    }
}
