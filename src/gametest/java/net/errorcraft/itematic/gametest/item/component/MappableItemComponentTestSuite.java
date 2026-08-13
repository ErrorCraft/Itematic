package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class MappableItemComponentTestSuite {
    @GameTest
    public void usingMapFillsMap(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        ItemStack map = world.itematic$createStack(ItemIds.MAP);
        player.setItemInHand(InteractionHand.MAIN_HAND, map);
        world.addFreshEntity(player);
        InteractionResult result = map.use(world, player, InteractionHand.MAIN_HAND);
        context.succeedIf(() -> Assert.isInstance(
            context,
            result,
            InteractionResult.Success.class,
            () -> "Expected mappable item usage to be successful",
            success -> Assert.itemStack(context, success.heldItemTransformedTo())
                .is(ItemIds.FILLED_MAP)
                .hasComponent(DataComponents.MAP_ID)
        ));
    }
}
