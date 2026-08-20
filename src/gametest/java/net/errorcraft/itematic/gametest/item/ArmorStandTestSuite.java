package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class ArmorStandTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 0);
    private static final BlockPos PLACED_ENTITY_POSITION = GROUND_POSITION.offset(0, 1, 0);
    private static final BlockPos HIGH_POSITION = GROUND_POSITION.offset(0, 3, 0);
    private static final float USER_ANGLE = 45.0f;
    private static final float SPAWNED_ENTITY_ANGLE = USER_ANGLE - 180.0f;

    @GameTest(structure = "itematic:item.armor_stand.platform")
    public void usingArmorStandOnGroundPlacesArmorStand(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.ARMOR_STAND)
        );
        level.addFreshEntity(player);
        TestUtil.interactWithBlock(helper, GROUND_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.ARMOR_STAND)
            .existsAt(PLACED_ENTITY_POSITION));
    }

    @GameTest(structure = "itematic:item.armor_stand.platform.high")
    public void usingArmorStandOnCeilingDoesNotPlaceArmorStand(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.ARMOR_STAND)
        );
        level.addFreshEntity(player);
        TestUtil.interactWithBlock(helper, HIGH_POSITION, player, Direction.DOWN);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.ARMOR_STAND)
            .doesNotExist());
    }

    @GameTest(structure = "itematic:item.armor_stand.platform.not_enough_room")
    public void usingArmorStandOnGroundWithNotEnoughRoomDoesNotPlaceArmorStand(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.ARMOR_STAND)
        );
        level.addFreshEntity(player);
        TestUtil.interactWithBlock(helper, GROUND_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.ARMOR_STAND)
            .doesNotExist());
    }

    @GameTest(structure = "itematic:item.armor_stand.platform")
    public void usingArmorStandOnGroundWhileRotatedPlacesArmorStandRotated(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.ARMOR_STAND)
        );
        player.setYRot(USER_ANGLE);
        level.addFreshEntity(player);
        TestUtil.interactWithBlock(helper, GROUND_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.ARMOR_STAND)
            .existsAt(
                PLACED_ENTITY_POSITION,
                armorStand -> armorStand.yaw(yaw -> yaw.equals(SPAWNED_ENTITY_ANGLE))
            ));
    }
}
