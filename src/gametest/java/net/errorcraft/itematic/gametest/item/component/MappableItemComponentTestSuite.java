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
    public void usingMapFillsMap(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        ItemStack map = level.itematic$createStack(ItemIds.MAP);
        player.setItemInHand(InteractionHand.MAIN_HAND, map);
        level.addFreshEntity(player);
        helper.succeedIf(() -> {
            InteractionResult result = map.use(level, player, InteractionHand.MAIN_HAND);
            Assert.interactionResult(helper, result, "mappable item usage")
                .resultStack(stack -> stack.is(ItemIds.FILLED_MAP)
                    .hasComponent(DataComponents.MAP_ID));
        });
    }
}
