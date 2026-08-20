package net.errorcraft.itematic.gametest.item.component;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class EntityItemComponentTestSuite {
    private static final BlockPos GROUND_POSITION = new BlockPos(1, 0, 0);
    private static final BlockPos PLACED_ENTITY_POSITION = GROUND_POSITION.offset(0, 1, 0);

    @GameTest(structure = "itematic:item.component.entity.platform")
    public void usingOakBoatOnGroundPlacesOakBoat(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack oakBoat = level.itematic$createStack(ItemIds.OAK_BOAT);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.OAK_BOAT)
        );
        level.addFreshEntity(player);
        TestUtil.interactWithBlock(helper, GROUND_POSITION, player, Direction.UP);
        helper.succeedIf(() -> helper.assertEntityPresent(EntityType.OAK_BOAT, PLACED_ENTITY_POSITION));
    }

    @GameTest(structure = "itematic:item.component.entity.platform")
    public void usingPigSpawnEggOnGroundPlacesPig(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.PIG_SPAWN_EGG)
        );
        level.addFreshEntity(player);
        TestUtil.interactWithBlock(helper, GROUND_POSITION, player, Direction.UP);
        helper.succeedIf(() -> helper.assertEntityPresent(EntityType.PIG, PLACED_ENTITY_POSITION));
    }
}
