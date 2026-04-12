package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.GameMode;

import java.util.Optional;

public class CompassTestSuite {
    private static final BlockPos LODESTONE_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.compass.platform.lodestone")
    public void usingCompassOnLodestoneSetsTrackedDataFromBlock(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack compass = world.itematic$createStack(ItemKeys.COMPASS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, compass);
        world.spawnEntity(player);
        ItemStack resultStack = TestUtil.useStackOnBlockInside(context, player, compass, LODESTONE_POSITION, Direction.UP)
            .orElseThrow();
        context.addFinalTask(() -> Assert.itemStack(context, resultStack)
            .hasComponent(DataComponentTypes.LODESTONE_TRACKER, lodestoneTracker -> {
                Assert.isTrue(
                    context,
                    lodestoneTracker.tracked(),
                    () -> "Expected Lodestone Compass to be tracked"
                );
                Optional<GlobalPos> target = lodestoneTracker.target();
                Assert.isTrue(
                    context,
                    target.isPresent(),
                    () -> "Expected Lodestone Compass to have a target"
                );
                Assert.areEqual(
                    context,
                    target.get().dimension(),
                    context.getWorld().getRegistryKey(),
                    "Lodestone dimension"
                );
                Assert.areEqual(
                    context,
                    target.get().pos(),
                    context.getAbsolutePos(LODESTONE_POSITION),
                    "Lodestone position"
                );
            }));
    }

    @GameTest(structure = "itematic:item.compass.platform.lodestone")
    public void destroyingLodestoneRemovesTrackedDataFromLodestoneCompass(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack compass = world.itematic$createStack(ItemKeys.COMPASS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, compass);
        world.spawnEntity(player);
        ItemStack resultStack = TestUtil.useStackOnBlockInside(context, player, compass, LODESTONE_POSITION, Direction.UP)
            .orElseThrow();
        context.removeBlock(LODESTONE_POSITION);
        context.createTimedTaskRunner()
            .expectMinDurationAndRun(1, () -> Assert.itemStack(context, resultStack)
                .hasComponent(DataComponentTypes.LODESTONE_TRACKER, lodestoneTracker -> {
                    Assert.isTrue(
                        context,
                        lodestoneTracker.tracked(),
                        () -> "Expected Lodestone Compass to be tracked"
                    );
                    Assert.isTrue(
                        context,
                        lodestoneTracker.target().isEmpty(),
                        () -> "Expected Lodestone Compass not to have a target"
                    );
                })
            )
            .completeIfSuccessful();
    }
}
