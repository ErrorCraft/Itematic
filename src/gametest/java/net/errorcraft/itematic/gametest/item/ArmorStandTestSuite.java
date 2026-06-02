package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class ArmorStandTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 0);
    private static final BlockPos PLACED_ENTITY_POSITION = GROUND_POSITION.add(0, 1, 0);
    private static final BlockPos HIGH_POSITION = GROUND_POSITION.add(0, 3, 0);
    private static final float USER_ANGLE = 45.0f;
    private static final float SPAWNED_ENTITY_ANGLE = USER_ANGLE - 180.0f;

    @GameTest(structure = "itematic:item.armor_stand.platform")
    public void usingArmorStandOnGroundPlacesArmorStand(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.ARMOR_STAND);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.ARMOR_STAND)
            .existsAt(PLACED_ENTITY_POSITION));
    }

    @GameTest(structure = "itematic:item.armor_stand.platform.high")
    public void usingArmorStandOnCeilingDoesNotPlaceArmorStand(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.ARMOR_STAND));
        world.spawnEntity(player);
        TestUtil.useBlock(context, HIGH_POSITION, player, Direction.DOWN);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.ARMOR_STAND)
            .doesNotExist());
    }

    @GameTest(structure = "itematic:item.armor_stand.platform.not_enough_room")
    public void usingArmorStandOnGroundWithNotEnoughRoomDoesNotPlaceArmorStand(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.ARMOR_STAND));
        world.spawnEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.ARMOR_STAND)
            .doesNotExist());
    }

    @GameTest(structure = "itematic:item.armor_stand.platform")
    public void usingArmorStandOnGroundWhileRotatedPlacesArmorStandRotated(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.ARMOR_STAND));
        player.setYaw(USER_ANGLE);
        world.spawnEntity(player);
        TestUtil.useBlock(context, GROUND_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.ARMOR_STAND)
            .existsAt(
                PLACED_ENTITY_POSITION,
                armorStand -> armorStand.yaw(yaw -> yaw.equals(SPAWNED_ENTITY_ANGLE))
            ));
    }
}
