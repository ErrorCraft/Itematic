package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class CrossbowTestSuite {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void usingCrossbowWithInfinityChargesArrowFromInventoryButDoesNotConsumeTheArrow(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CROSSBOW);
        stack.addEnchantment(Enchantments.INFINITY, 1);
        ItemStack ammunition = world.itematic$createStack(ItemKeys.ARROW);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        player.getInventory().insertStack(ammunition);
        world.spawnEntity(player);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> stack.use(world, player, Hand.MAIN_HAND))
            .expectMinDurationAndRun(stack.itematic$useDuration(player), () -> {
                player.stopUsingItem();
                Assert.itemStackHasDataComponent(player.getStackInHand(Hand.MAIN_HAND), DataComponentTypes.CHARGED_PROJECTILES,
                    component -> context.assertTrue(component.itematic$contains(ItemKeys.ARROW), "Expected item stack to have an Arrow as a charged projectile")
                );
                context.assertTrue(player.getInventory().contains(s -> s.itematic$isOf(ItemKeys.ARROW)), "Expected player to have an Arrow in their inventory");
            })
            .completeIfSuccessful();
    }
}
