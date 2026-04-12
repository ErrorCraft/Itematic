package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class MappableItemComponentTestSuite {
    @GameTest
    public void usingMapFillsMap(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack map = world.itematic$createStack(ItemKeys.MAP);
        player.setStackInHand(Hand.MAIN_HAND, map);
        world.spawnEntity(player);
        ActionResult result = map.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> Assert.isInstance(
            context,
            result,
            ActionResult.Success.class,
            () -> "Expected mappable item usage to be successful",
            success -> Assert.itemStack(context, success.getNewHandStack())
                .is(ItemKeys.FILLED_MAP)
                .hasComponent(DataComponentTypes.MAP_ID)
        ));
    }
}
