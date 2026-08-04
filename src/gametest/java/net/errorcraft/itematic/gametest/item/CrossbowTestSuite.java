package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;

public class CrossbowTestSuite {
    @GameTest(maxTicks = 100)
    public void usingCrossbowWithInfinityChargesArrowFromInventoryButDoesNotConsumeTheArrow(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack crossbow = TestUtil.createItemStackWithEnchantment(world, ItemKeys.CROSSBOW, Enchantments.INFINITY);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, crossbow);
        player.getInventory().add(world.itematic$createStack(ItemKeys.ARROW));
        world.addFreshEntity(player);
        context.startSequence()
            .thenExecute(() -> crossbow.use(world, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(crossbow.getUseDuration(player), () -> {
                player.releaseUsingItem();
                Assert.itemStack(context, player.getItemInHand(InteractionHand.MAIN_HAND))
                    .hasComponent(
                        DataComponents.CHARGED_PROJECTILES,
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
            .thenSucceed();
    }
}
