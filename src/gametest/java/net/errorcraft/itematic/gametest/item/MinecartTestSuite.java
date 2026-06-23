package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class MinecartTestSuite {
    private static final BlockPos RAIL_POSITION = new BlockPos(1, 1, 1);
    private static final BlockPos PLACED_ENTITY_POSITION = RAIL_POSITION;
    private static final double PLACED_ENTITY_VERTICAL_OFFSET = 0.0625d;
    private static final double PLACED_ENTITY_VERTICAL_OFFSET_ASCENDING = PLACED_ENTITY_VERTICAL_OFFSET + 0.5d;

    @GameTest(structure = "itematic:item.minecart.platform")
    public void usingMinecartWithoutRailsDoesNotPlaceMinecart(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.MINECART));
        world.spawnEntity(player);
        TestUtil.useBlock(context, RAIL_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.MINECART)
            .doesNotExist());
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.north_south")
    public void usingMinecartOnNorthSouthRailsPlacesMinecartAtCorrectPosition(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.MINECART));
        world.spawnEntity(player);
        TestUtil.useBlock(context, RAIL_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.east_west")
    public void usingMinecartOnEastWestRailsPlacesMinecartAtCorrectPosition(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.MINECART));
        world.spawnEntity(player);
        TestUtil.useBlock(context, RAIL_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.ascending_east")
    public void usingMinecartOnAscendingEastRailsPlacesMinecartAtCorrectPosition(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.MINECART));
        world.spawnEntity(player);
        TestUtil.useBlock(context, RAIL_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET_ASCENDING)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.ascending_west")
    public void usingMinecartOnAscendingWestRailsPlacesMinecartAtCorrectPosition(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.MINECART));
        world.spawnEntity(player);
        TestUtil.useBlock(context, RAIL_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET_ASCENDING)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.ascending_north")
    public void usingMinecartOnAscendingNorthRailsPlacesMinecartAtCorrectPosition(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.MINECART));
        world.spawnEntity(player);
        TestUtil.useBlock(context, RAIL_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET_ASCENDING)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.ascending_south")
    public void usingMinecartOnAscendingSouthRailsPlacesMinecartAtCorrectPosition(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.MINECART));
        world.spawnEntity(player);
        TestUtil.useBlock(context, RAIL_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET_ASCENDING)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.south_east")
    public void usingMinecartOnSouthEastRailsPlacesMinecartAtCorrectPosition(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.MINECART));
        world.spawnEntity(player);
        TestUtil.useBlock(context, RAIL_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.south_west")
    public void usingMinecartOnSouthWestRailsPlacesMinecartAtCorrectPosition(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.MINECART));
        world.spawnEntity(player);
        TestUtil.useBlock(context, RAIL_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.north_west")
    public void usingMinecartOnNorthWestRailsPlacesMinecartAtCorrectPosition(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.MINECART));
        world.spawnEntity(player);
        TestUtil.useBlock(context, RAIL_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.north_east")
    public void usingMinecartOnNorthEastRailsPlacesMinecartAtCorrectPosition(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.MINECART));
        world.spawnEntity(player);
        TestUtil.useBlock(context, RAIL_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecart -> minecart.y(
                y -> y.congruent(
                    1.0d,
                    congruentY -> congruentY.equals(PLACED_ENTITY_VERTICAL_OFFSET)
                )
            )));
    }

    @GameTest(structure = "itematic:item.minecart.platform.rails.north_south")
    public void placingMinecartSetsCustomNameFromItemStack(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack minecart = world.itematic$createStack(ItemKeys.MINECART);
        Text customName = Text.literal("abc");
        minecart.set(DataComponentTypes.CUSTOM_NAME, customName);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, minecart);
        world.spawnEntity(player);
        TestUtil.useBlock(context, RAIL_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.MINECART)
            .existsAt(PLACED_ENTITY_POSITION, minecartEntity -> minecartEntity.test(
                Entity::getCustomName,
                customEntityName -> Assert.areEqual(
                    context,
                    customEntityName,
                    customName,
                    "custom name"
                )
            )));
    }
}
