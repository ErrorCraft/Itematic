package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
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
    public void usingCrossbowWithInfinityChargesArrowFromInventoryButDoesNotConsumeTheArrow(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack crossbow = TestUtil.createItemStackWithEnchantment(level, ItemIds.CROSSBOW, Enchantments.INFINITY);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, crossbow);
        player.getInventory().add(level.itematic$createStack(ItemIds.ARROW));
        level.addFreshEntity(player);
        helper.startSequence()
            .thenExecute(() -> crossbow.use(level, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(crossbow.getUseDuration(player), () -> {
                player.releaseUsingItem();
                Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                    .hasComponent(
                        DataComponents.CHARGED_PROJECTILES,
                        component -> Assert.isTrue(
                            helper,
                            component.itematic$contains(ItemIds.ARROW),
                            () -> "Expected item stack to have an Arrow as a charged projectile"
                        )
                    );
                Assert.isTrue(
                    helper,
                    player.getInventory().contains(stack -> stack.is(ItemIds.ARROW)),
                    () -> "Expected Player to have an Arrow in their inventory"
                );
            })
            .thenSucceed();
    }
}
