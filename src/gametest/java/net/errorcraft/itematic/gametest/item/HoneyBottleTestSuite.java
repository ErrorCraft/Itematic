package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class HoneyBottleTestSuite {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void consumingHoneyBottleRemovesPoisonStatusEffect(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.getHungerManager().setFoodLevel(0);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, StatusEffectInstance.INFINITE));
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.HONEY_BOTTLE);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        stack.use(world, player, Hand.MAIN_HAND);
        context.createTimedTaskRunner().expectMinDurationAndRun(
            TestUtil.getItemComponent(stack, ItemComponentTypes.USE_DURATION).ticks(),
            () -> Assert.entityDoesNotHaveStatusEffect(player, StatusEffects.POISON)
        ).completeIfSuccessful();
    }
}
