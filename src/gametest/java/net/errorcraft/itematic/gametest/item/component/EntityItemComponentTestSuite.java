package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class EntityItemComponentTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 0);
    private static final BlockPos PLACED_ENTITY_POSITION = GROUND_POSITION.add(0, 1, 0);

    @GameTest(templateName = "itematic:item.component.entity.platform")
    public void usingOakBoatOnGroundPlacesOakBoat(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OAK_BOAT);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.expectEntityAt(EntityType.OAK_BOAT, PLACED_ENTITY_POSITION));
    }

    @GameTest(templateName = "itematic:item.component.entity.platform")
    public void usingPigSpawnEggOnGroundPlacesPig(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.PIG_SPAWN_EGG);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, GROUND_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> context.expectEntityAt(EntityType.PIG, PLACED_ENTITY_POSITION));
    }
}
