package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class EntityItemComponentTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 0);
    private static final BlockPos PLACED_ENTITY_POSITION = GROUND_POSITION.offset(0, 1, 0);

    @GameTest(structure = "itematic:item.component.entity.platform")
    public void usingOakBoatOnGroundPlacesOakBoat(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack oakBoat = world.itematic$createStack(ItemKeys.OAK_BOAT);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oakBoat);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakBoat, GROUND_POSITION, Direction.UP);
        context.succeedIf(() -> context.assertEntityPresent(EntityType.OAK_BOAT, PLACED_ENTITY_POSITION));
    }

    @GameTest(structure = "itematic:item.component.entity.platform")
    public void usingPigSpawnEggOnGroundPlacesPig(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack pigSpawnEgg = world.itematic$createStack(ItemKeys.PIG_SPAWN_EGG);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, pigSpawnEgg);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, pigSpawnEgg, GROUND_POSITION, Direction.UP);
        context.succeedIf(() -> context.assertEntityPresent(EntityType.PIG, PLACED_ENTITY_POSITION));
    }
}
