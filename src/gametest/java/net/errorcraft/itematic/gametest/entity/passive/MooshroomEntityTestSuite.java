package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class MooshroomEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    public void usingFlowerOnBrownMooshroomGivesMooshroomSuspiciousEffects(GameTestHelper context) {
        MushroomCow mooshroom = context.spawn(EntityType.MOOSHROOM, SPAWN_POSITION);
        mooshroom.setComponent(DataComponents.MOOSHROOM_VARIANT, MushroomCow.Variant.BROWN);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        context.succeedIf(() -> {
            ServerLevel world = context.getLevel();
            player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemIds.DANDELION));
            InteractionResult dandelionResult = mooshroom.mobInteract(player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                dandelionResult.consumesAction(),
                () -> "Expected interaction with Dandelion on brown Mooshroom to be successful"
            );
            player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemIds.BOWL));
            InteractionResult bowlResult = mooshroom.mobInteract(player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                bowlResult.consumesAction(),
                () -> "Expected interaction with Bowl on brown Mooshroom to be successful"
            );
            Assert.itemStack(context, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.SUSPICIOUS_STEW)
                .hasComponent(
                    DataComponents.SUSPICIOUS_STEW_EFFECTS,
                    suspiciousStewEffects -> Assert.isFalse(
                        context,
                        suspiciousStewEffects.effects().isEmpty(),
                        () -> "Expected item stack to have suspicious effects"
                    )
                );
        });
    }

    @GameTest(structure = "itematic:entity.platform")
    public void usingBowlOnMooshroomGivesMushroomStew(GameTestHelper context) {
        MushroomCow mooshroom = context.spawn(EntityType.MOOSHROOM, SPAWN_POSITION);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        context.succeedIf(() -> {
            player.setItemInHand(InteractionHand.MAIN_HAND, context.getLevel().itematic$createStack(ItemIds.BOWL));
            InteractionResult bowlResult = mooshroom.mobInteract(player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                bowlResult.consumesAction(),
                () -> "Expected interaction with Bowl on Mooshroom to be successful"
            );
            Assert.itemStack(context, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.MUSHROOM_STEW);
        });
    }
}
