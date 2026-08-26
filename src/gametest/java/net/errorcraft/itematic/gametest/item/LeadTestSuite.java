package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class LeadTestSuite {
    private static final BlockPos PLACED_ENTITY_POSITION = new BlockPos(1, 1, 0);

    @GameTest(structure = "itematic:item.lead.platform")
    public void usingLeadOnHorseLeashesHorse(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack lead = level.itematic$createStack(ItemIds.LEAD);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, lead);
        level.addFreshEntity(player);
        Horse horse = TestUtil.createEntityAt(helper, EntityType.HORSE, PLACED_ENTITY_POSITION, entity -> {
        });
        helper.succeedIf(() -> {
            InteractionResult result = TestUtil.interactWithEntity(horse, player);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected interaction with Horse to be successful"
            );
            Assert.isTrue(
                helper,
                horse.isLeashed(),
                () -> "Expected Horse to be leashed"
            );
        });
    }

    @GameTest(structure = "itematic:item.lead.platform")
    public void usingLeadOnBoatLeashesBoat(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack lead = level.itematic$createStack(ItemIds.LEAD);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, lead);
        level.addFreshEntity(player);
        Boat boat = TestUtil.createEntityAt(helper, EntityType.OAK_BOAT, PLACED_ENTITY_POSITION, entity -> {
        });
        helper.succeedIf(() -> {
            InteractionResult result = TestUtil.interactWithEntity(boat, player);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected interaction with Oak Boat to be successful"
            );
            Assert.isTrue(
                helper,
                boat.isLeashed(),
                () -> "Expected Oak Boat to be leashed"
            );
        });
    }
}
