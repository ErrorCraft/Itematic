package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class LeadTestSuite {
    private static final BlockPos PLACED_ENTITY_POSITION = new BlockPos(1, 2, 0);

    @GameTest(templateName = "itematic:item.lead.platform")
    public void usingLeadOnHorseLeashesHorse(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.LEAD);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        HorseEntity horse = TestUtil.createEntityAt(context, EntityType.HORSE, PLACED_ENTITY_POSITION, entity -> {});
        context.addInstantFinalTask(() -> {
            ActionResult result = horse.interact(player, Hand.MAIN_HAND);
            context.assertTrue(result.isAccepted(), "Expected interaction with Horse to be successful");
            context.assertTrue(horse.isLeashed(), "Expected Horse to be leashed");
        });
    }

    @GameTest(templateName = "itematic:item.lead.platform")
    public void usingLeadOnBoatLeashesBoat(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.LEAD);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        BoatEntity boat = TestUtil.createEntityAt(context, EntityType.BOAT, PLACED_ENTITY_POSITION, entity -> {});
        context.addInstantFinalTask(() -> {
            ActionResult result = boat.interact(player, Hand.MAIN_HAND);
            context.assertTrue(result.isAccepted(), "Expected interaction with Boat to be successful");
            context.assertTrue(boat.isLeashed(), "Expected Boat to be leashed");
        });
    }
}
