package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class MinecartTestSuite {
    private static final BlockPos RAIL_POSITION = new BlockPos(1, 1, 1);
    private static final BlockPos PLACED_ENTITY_POSITION = RAIL_POSITION;
    private static final double PLACED_ENTITY_VERTICAL_OFFSET = 0.0625d;
    private static final double PLACED_ENTITY_VERTICAL_OFFSET_ASCENDING = PLACED_ENTITY_VERTICAL_OFFSET + 0.5d;

    @GameTest(structure = "itematic:item.minecart.platform")
    public void usingMinecartWithoutRailsDoesNotPlaceMinecart(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.MINECART)
        );
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, RAIL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.MINECART)
            .doesNotExist());
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.north_south")
    public void usingMinecartOnNorthSouthRailsPlacesMinecartAtCorrectPosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.MINECART)
        );
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, RAIL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.east_west")
    public void usingMinecartOnEastWestRailsPlacesMinecartAtCorrectPosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.MINECART)
        );
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, RAIL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.ascending_east")
    public void usingMinecartOnAscendingEastRailsPlacesMinecartAtCorrectPosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.MINECART)
        );
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, RAIL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET_ASCENDING)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.ascending_west")
    public void usingMinecartOnAscendingWestRailsPlacesMinecartAtCorrectPosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.MINECART)
        );
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, RAIL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET_ASCENDING)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.ascending_north")
    public void usingMinecartOnAscendingNorthRailsPlacesMinecartAtCorrectPosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.MINECART)
        );
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, RAIL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET_ASCENDING)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.ascending_south")
    public void usingMinecartOnAscendingSouthRailsPlacesMinecartAtCorrectPosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.MINECART)
        );
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, RAIL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET_ASCENDING)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.south_east")
    public void usingMinecartOnSouthEastRailsPlacesMinecartAtCorrectPosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.MINECART)
        );
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, RAIL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.south_west")
    public void usingMinecartOnSouthWestRailsPlacesMinecartAtCorrectPosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.MINECART)
        );
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, RAIL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.north_west")
    public void usingMinecartOnNorthWestRailsPlacesMinecartAtCorrectPosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.MINECART)
        );
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, RAIL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.north_east")
    public void usingMinecartOnNorthEastRailsPlacesMinecartAtCorrectPosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.MINECART)
        );
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, RAIL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.north_south")
    public void placingMinecartSetsCustomNameFromItemStack(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack minecart = level.itematic$createStack(ItemIds.MINECART);
        Component customName = Component.literal("abc");
        minecart.set(DataComponents.CUSTOM_NAME, customName);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, minecart);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, RAIL_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecartEntity -> minecartEntity.test(
                Entity::getCustomName,
                customEntityName -> Assert.areEqual(
                    helper,
                    customEntityName,
                    customName,
                    "custom name"
                )
            )));
    }
}
