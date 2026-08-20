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
    public void usingCompassOnLodestoneSetsTrackedDataFromBlock(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack compass = level.itematic$createStack(ItemIds.COMPASS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, compass);
        level.addFreshEntity(player);
        ItemStack resultStack = TestUtil.useStackOnBlockInside(helper, player, compass, LODESTONE_POSITION, Direction.UP)
            .orElseThrow();
        helper.succeedIf(() -> Assert.itemStack(helper, resultStack)
            .hasComponent(DataComponents.LODESTONE_TRACKER, lodestoneTracker -> {
                Assert.isTrue(
                    helper,
                    lodestoneTracker.tracked(),
                    () -> "Expected Lodestone Compass to be tracked"
                );
                Optional<GlobalPos> target = lodestoneTracker.target();
                Assert.isTrue(
                    helper,
                    target.isPresent(),
                    () -> "Expected Lodestone Compass to have a target"
                );
                Assert.areEqual(
                    helper,
                    target.get().dimension(),
                    helper.getLevel().dimension(),
                    "Lodestone dimension"
                );
                Assert.areEqual(
                    helper,
                    target.get().pos(),
                    helper.absolutePos(LODESTONE_POSITION),
                    "Lodestone position"
                );
            }));
    }

    @GameTest(structure = "itematic:item.compass.platform.lodestone")
    public void destroyingLodestoneRemovesTrackedDataFromLodestoneCompass(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack compass = level.itematic$createStack(ItemIds.COMPASS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, compass);
        level.addFreshEntity(player);
        ItemStack resultStack = TestUtil.useStackOnBlockInside(helper, player, compass, LODESTONE_POSITION, Direction.UP)
            .orElseThrow();
        helper.destroyBlock(LODESTONE_POSITION);
        helper.startSequence()
            .thenExecuteAfter(1, () -> Assert.itemStack(helper, resultStack)
                .hasComponent(DataComponents.LODESTONE_TRACKER, lodestoneTracker -> {
                    Assert.isTrue(
                        helper,
                        lodestoneTracker.tracked(),
                        () -> "Expected Lodestone Compass to be tracked"
                    );
                    Assert.isTrue(
                        helper,
                        lodestoneTracker.target().isEmpty(),
                        () -> "Expected Lodestone Compass not to have a target"
                    );
                })
            )
            .thenSucceed();
    }
}
