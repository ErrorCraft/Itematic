package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class EndCrystalTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 1);
    private static final BlockPos PLACED_ENTITY_POSITION = GROUND_POSITION.add(0, 1, 0);

    @GameTest(structure = "itematic:item.end_crystal.platform")
    public void usingEndCrystalOnGroundPlacesEndCrystal(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.END_CRYSTAL));
        world.spawnEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.END_CRYSTAL)
            .existsAt(
                PLACED_ENTITY_POSITION,
                endCrystal -> endCrystal.test(
                    EndCrystalEntity::shouldShowBottom,
                    shouldShowBottom -> Assert.isFalse(
                        context,
                        shouldShowBottom,
                        () -> "Expected End Crystal not to show its bottom"
                    )
                )
            ));
    }

    @GameTest(structure = "itematic:item.end_crystal.platform.unsupported")
    public void usingEndCrystalOnUnsupportedBlockDoesNotPlaceEndCrystal(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.END_CRYSTAL));
        world.spawnEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.END_CRYSTAL)
            .doesNotExist());
    }

    @GameTest(structure = "itematic:item.end_crystal.platform.without_air")
    public void usingEndCrystalWithoutAirBlockDoesNotPlaceEndCrystal(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.END_CRYSTAL));
        world.spawnEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.END_CRYSTAL)
            .doesNotExist());
    }

    @GameTest(structure = "itematic:item.end_crystal.platform.not_enough_room_from_interfering_entity")
    public void usingEndCrystalOnGroundWithNotEnoughRoomFromInterferingEntityDoesNotPlaceEndCrystal(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.END_CRYSTAL));
        world.spawnEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.END_CRYSTAL)
            .doesNotExist());
    }
}
