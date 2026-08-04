package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class WolfEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    @SuppressWarnings("removal")
    public void holdingBoneTemptsWolf(GameTestHelper context) {
        Wolf wolf = context.spawn(EntityType.WOLF, SPAWN_POSITION);
        wolf.setTame(true, true);
        ServerPlayer player = context.makeMockServerPlayerInLevel();
        player.setItemInHand(InteractionHand.MAIN_HAND, context.getLevel().itematic$createStack(ItemKeys.BONE));
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        context.succeedWhen(() -> Assert.isTrue(
            context,
            wolf.isInterested(),
            () -> "Expected wolf to be begging"
        ));
    }

    @GameTest(structure = "itematic:entity.platform")
    @SuppressWarnings("removal")
    public void holdingMeatTemptsWolf(GameTestHelper context) {
        Wolf wolf = context.spawn(EntityType.WOLF, SPAWN_POSITION);
        wolf.setTame(true, true);
        ServerPlayer player = context.makeMockServerPlayerInLevel();
        player.setItemInHand(InteractionHand.MAIN_HAND, context.getLevel().itematic$createStack(ItemKeys.BEEF));
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        context.succeedWhen(() -> Assert.isTrue(
            context,
            wolf.isInterested(),
            () -> "Expected wolf to be begging"
        ));
    }

    @GameTest(structure = "itematic:entity.platform")
    public void feedingWolfMeatHealsWolf(GameTestHelper context) {
        Wolf wolf = context.spawn(EntityType.WOLF, SPAWN_POSITION);
        wolf.setTame(true, true);
        context.withLowHealth(wolf);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, context.getLevel().itematic$createStack(ItemKeys.BEEF));
        wolf.mobInteract(player, InteractionHand.MAIN_HAND);
        context.succeedIf(() -> Assert.livingEntity(context, wolf)
            .hasHealth(health -> health.isGreaterThan(0.25f)));
    }
}
