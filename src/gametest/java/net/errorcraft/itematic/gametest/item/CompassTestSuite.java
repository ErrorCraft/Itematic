package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.GameMode;

import java.util.Optional;

public class CompassTestSuite {
    private static final BlockPos LODESTONE_POSITION = new BlockPos(1, 1, 1);

    @GameTest(templateName = "itematic:item.compass.platform.lodestone")
    public void usingCompassOnLodestoneSetsTrackedDataFromBlock(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.COMPASS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        ItemStack resultStack = TestUtil.useStackOnBlockInside(context, player, stack, LODESTONE_POSITION, Direction.UP)
            .orElseThrow();
        context.addInstantFinalTask(() -> {
            Assert.itemStackHasDataComponent(resultStack, DataComponentTypes.LODESTONE_TRACKER, lodestoneTracker -> {
                context.assertTrue(lodestoneTracker.tracked(), "Expected Lodestone Compass to be tracked");
                Optional<GlobalPos> target = lodestoneTracker.target();
                context.assertTrue(target.isPresent(), "Expected Lodestone Compass to have a target");
                context.assertEquals(
                    target.orElseThrow().dimension(),
                    context.getWorld().getRegistryKey(),
                    "Lodestone dimension"
                );
                context.assertEquals(
                    target.orElseThrow().pos(),
                    context.getAbsolutePos(LODESTONE_POSITION),
                    "Lodestone position"
                );
            });
        });
    }

    @GameTest(templateName = "itematic:item.compass.platform.lodestone")
    public void destroyingLodestoneRemovesTrackedDataFromLodestoneCompass(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.COMPASS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        ItemStack resultStack = TestUtil.useStackOnBlockInside(context, player, stack, LODESTONE_POSITION, Direction.UP)
            .orElseThrow();
        context.removeBlock(LODESTONE_POSITION);
        context.createTimedTaskRunner()
            .expectMinDurationAndRun(1, () -> {
                Assert.itemStackHasDataComponent(resultStack, DataComponentTypes.LODESTONE_TRACKER, lodestoneTracker -> {
                    context.assertTrue(lodestoneTracker.tracked(), "Expected Lodestone Compass to be tracked");
                    context.assertTrue(lodestoneTracker.target().isEmpty(), "Expected Lodestone Compass not to have a target");
                });
            })
            .completeIfSuccessful();
    }
}
