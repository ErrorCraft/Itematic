package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import java.util.Optional;

public class CompassTestSuite {
    private static final BlockPos LODESTONE_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.compass.platform.lodestone")
    public void usingCompassOnLodestoneSetsTrackedDataFromBlock(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack compass = world.itematic$createStack(ItemIds.COMPASS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, compass);
        world.addFreshEntity(player);
        ItemStack resultStack = TestUtil.useStackOnBlockInside(context, player, compass, LODESTONE_POSITION, Direction.UP)
            .orElseThrow();
        context.succeedIf(() -> Assert.itemStack(context, resultStack)
            .hasComponent(DataComponents.LODESTONE_TRACKER, lodestoneTracker -> {
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
                    context.getLevel().dimension(),
                    "Lodestone dimension"
                );
                Assert.areEqual(
                    context,
                    target.get().pos(),
                    context.absolutePos(LODESTONE_POSITION),
                    "Lodestone position"
                );
            }));
    }

    @GameTest(structure = "itematic:item.compass.platform.lodestone")
    public void destroyingLodestoneRemovesTrackedDataFromLodestoneCompass(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack compass = world.itematic$createStack(ItemIds.COMPASS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, compass);
        world.addFreshEntity(player);
        ItemStack resultStack = TestUtil.useStackOnBlockInside(context, player, compass, LODESTONE_POSITION, Direction.UP)
            .orElseThrow();
        context.destroyBlock(LODESTONE_POSITION);
        context.startSequence()
            .thenExecuteAfter(1, () -> Assert.itemStack(context, resultStack)
                .hasComponent(DataComponents.LODESTONE_TRACKER, lodestoneTracker -> {
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
            .thenSucceed();
    }
}
