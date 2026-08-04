package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class EndCrystalTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 1);
    private static final BlockPos PLACED_ENTITY_POSITION = GROUND_POSITION.offset(0, 1, 0);

    @GameTest(structure = "itematic:item.end_crystal.platform")
    public void usingEndCrystalOnGroundPlacesEndCrystal(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemKeys.END_CRYSTAL));
        world.addFreshEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.succeedIf(() -> Assert.entityType(context, EntityType.END_CRYSTAL)
            .existsAt(
                PLACED_ENTITY_POSITION,
                endCrystal -> endCrystal.test(
                    EndCrystal::showsBottom,
                    shouldShowBottom -> Assert.isFalse(
                        context,
                        shouldShowBottom,
                        () -> "Expected End Crystal not to show its bottom"
                    )
                )
            ));
    }

    @GameTest(structure = "itematic:item.end_crystal.platform.unsupported")
    public void usingEndCrystalOnUnsupportedBlockDoesNotPlaceEndCrystal(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemKeys.END_CRYSTAL));
        world.addFreshEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.succeedIf(() -> Assert.entityType(context, EntityType.END_CRYSTAL)
            .doesNotExist());
    }

    @GameTest(structure = "itematic:item.end_crystal.platform.without_air")
    public void usingEndCrystalWithoutAirBlockDoesNotPlaceEndCrystal(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemKeys.END_CRYSTAL));
        world.addFreshEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.succeedIf(() -> Assert.entityType(context, EntityType.END_CRYSTAL)
            .doesNotExist());
    }

    @GameTest(structure = "itematic:item.end_crystal.platform.not_enough_room_from_interfering_entity")
    public void usingEndCrystalOnGroundWithNotEnoughRoomFromInterferingEntityDoesNotPlaceEndCrystal(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemKeys.END_CRYSTAL));
        world.addFreshEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.succeedIf(() -> Assert.entityType(context, EntityType.END_CRYSTAL)
            .doesNotExist());
    }
}
