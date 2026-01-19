package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class MooshroomEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 2, 1);

    @GameTest(templateName = "itematic:entity.platform")
    public void usingFlowerOnBrownMooshroomGivesMooshroomSuspiciousEffects(TestContext context) {
        MooshroomEntity mooshroom = context.spawnEntity(EntityType.MOOSHROOM, SPAWN_POSITION);
        mooshroom.setVariant(MooshroomEntity.Type.BROWN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.DANDELION);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        ActionResult result = mooshroom.interactMob(player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> {
            context.assertTrue(result.isAccepted(), "Expected interaction with dandelion on brown Mooshroom to be successful");
            player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.BOWL));
            ActionResult bowlResult = mooshroom.interactMob(player, Hand.MAIN_HAND);
            context.assertTrue(bowlResult.isAccepted(), "Expected interaction with bowl on brown Mooshroom to be successful");
            ItemStack heldStack = player.getStackInHand(Hand.MAIN_HAND);
            Assert.itemStackIsOf(heldStack, ItemKeys.SUSPICIOUS_STEW);
            Assert.itemStackHasDataComponent(heldStack, DataComponentTypes.SUSPICIOUS_STEW_EFFECTS,
                component -> context.assertTrue(!component.effects().isEmpty(), "Expected item stack to have suspicious effects")
            );
        });
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void usingBowlOnMooshroomGivesMushroomStew(TestContext context) {
        MooshroomEntity mooshroom = context.spawnEntity(EntityType.MOOSHROOM, SPAWN_POSITION);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BOWL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        ActionResult result = mooshroom.interactMob(player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> {
            context.assertTrue(result.isAccepted(), "Expected interaction with bowl on Mooshroom to be successful");
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.MUSHROOM_STEW);
        });
    }
}
