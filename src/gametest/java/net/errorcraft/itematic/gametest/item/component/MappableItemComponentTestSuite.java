package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class MappableItemComponentTestSuite {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void usingMapFillsMap(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.MAP);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        context.addInstantFinalTask(() -> {
            ActionResult result = stack.use(world, player, Hand.MAIN_HAND);
            if (!(result instanceof ActionResult.Success successResult)) {
                throw new GameTestException("Expected mappable usage to be successful");
            }

            ItemStack resultStack = successResult.getNewHandStack();
            Assert.itemStackIsOf(resultStack, ItemKeys.FILLED_MAP);
            Assert.itemStackHasDataComponent(resultStack, DataComponentTypes.MAP_ID);
        });
    }
}
