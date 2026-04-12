package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class MooshroomEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    public void usingFlowerOnBrownMooshroomGivesMooshroomSuspiciousEffects(TestContext context) {
        MooshroomEntity mooshroom = context.spawnEntity(EntityType.MOOSHROOM, SPAWN_POSITION);
        mooshroom.setComponent(DataComponentTypes.MOOSHROOM_VARIANT, MooshroomEntity.Variant.BROWN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        context.addFinalTask(() -> {
            ServerWorld world = context.getWorld();
            player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.DANDELION));
            ActionResult dandelionResult = mooshroom.interactMob(player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                dandelionResult.isAccepted(),
                () -> "Expected interaction with Dandelion on brown Mooshroom to be successful"
            );
            player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.BOWL));
            ActionResult bowlResult = mooshroom.interactMob(player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                bowlResult.isAccepted(),
                () -> "Expected interaction with Bowl on brown Mooshroom to be successful"
            );
            Assert.itemStack(context, player.getStackInHand(Hand.MAIN_HAND))
                .is(ItemKeys.SUSPICIOUS_STEW)
                .hasComponent(
                    DataComponentTypes.SUSPICIOUS_STEW_EFFECTS,
                    suspiciousStewEffects -> Assert.isFalse(
                        context,
                        suspiciousStewEffects.effects().isEmpty(),
                        () -> "Expected item stack to have suspicious effects"
                    )
                );
        });
    }

    @GameTest(structure = "itematic:entity.platform")
    public void usingBowlOnMooshroomGivesMushroomStew(TestContext context) {
        MooshroomEntity mooshroom = context.spawnEntity(EntityType.MOOSHROOM, SPAWN_POSITION);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        context.addFinalTask(() -> {
            player.setStackInHand(Hand.MAIN_HAND, context.getWorld().itematic$createStack(ItemKeys.BOWL));
            ActionResult bowlResult = mooshroom.interactMob(player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                bowlResult.isAccepted(),
                () -> "Expected interaction with Bowl on Mooshroom to be successful"
            );
            Assert.itemStack(context, player.getStackInHand(Hand.MAIN_HAND))
                .is(ItemKeys.MUSHROOM_STEW);
        });
    }
}
