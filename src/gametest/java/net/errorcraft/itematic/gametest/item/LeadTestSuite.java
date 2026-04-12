package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class LeadTestSuite {
    private static final BlockPos PLACED_ENTITY_POSITION = new BlockPos(1, 1, 0);

    @GameTest(structure = "itematic:item.lead.platform")
    public void usingLeadOnHorseLeashesHorse(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack lead = world.itematic$createStack(ItemKeys.LEAD);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, lead);
        world.spawnEntity(player);
        HorseEntity horse = TestUtil.createEntityAt(context, EntityType.HORSE, PLACED_ENTITY_POSITION, entity -> {});
        context.addFinalTask(() -> {
            ActionResult result = horse.interact(player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected interaction with Horse to be successful"
            );
            Assert.isTrue(
                context,
                horse.isLeashed(),
                () -> "Expected Horse to be leashed"
            );
        });
    }

    @GameTest(structure = "itematic:item.lead.platform")
    public void usingLeadOnBoatLeashesBoat(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack lead = world.itematic$createStack(ItemKeys.LEAD);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, lead);
        world.spawnEntity(player);
        BoatEntity boat = TestUtil.createEntityAt(context, EntityType.OAK_BOAT, PLACED_ENTITY_POSITION, entity -> {});
        context.addFinalTask(() -> {
            ActionResult result = boat.interact(player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected interaction with Oak Boat to be successful"
            );
            Assert.isTrue(
                context,
                boat.isLeashed(),
                () -> "Expected Oak Boat to be leashed"
            );
        });
    }
}
