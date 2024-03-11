package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class PotionItemComponentTestSuite {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void drinkingPotionItemAddsEffects(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack stack = PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.LEAPING);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        stack.use(world, player, Hand.MAIN_HAND);
        context.createTimedTaskRunner().expectMinDurationAndRun(
            TestUtil.getItemComponent(stack, ItemComponentTypes.USE_DURATION).ticks(),
            () -> Assert.forAll(
                Potions.LEAPING.value().getEffects(),
                effectInstance -> context.expectEntityHasEffect(player, effectInstance.getEffectType(), effectInstance.getAmplifier())
            )
        ).completeIfSuccessful();
    }
}
