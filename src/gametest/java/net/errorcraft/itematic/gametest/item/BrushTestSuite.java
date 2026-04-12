package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class BrushTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 2, 1);
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 0);

    @GameTest(structure = "itematic:item.brush.platform")
    public void usingBrushDoesNotStartBrushing(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack brush = world.itematic$createStack(ItemKeys.BRUSH);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, brush);
        brush.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> Assert.isFalse(
            context,
            player.isUsingItem(),
            () -> "Expected Player not to have started using a Brush"
        ));
    }

    @GameTest(structure = "itematic:item.brush.platform.suspicious_sand")
    public void usingBrushOnBlockStartsBrushing(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack brush = world.itematic$createStack(ItemKeys.BRUSH);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, brush);
        world.spawnEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.isTrue(
            context,
            player.isUsingItem(),
            () -> "Expected Player to have started using a Brush"
        ));
    }

    @GameTest(structure = "itematic:item.brush.platform.short_grass")
    public void usingBrushOnIntangibleBlockDoesNotStartBrushing(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack brush = world.itematic$createStack(ItemKeys.BRUSH);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, brush);
        world.spawnEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.UP);
        context.addFinalTaskWithDuration(1, () -> Assert.isFalse(
            context,
            player.isUsingItem(),
            () -> "Expected Player not to have started using a Brush"
        ));
    }
}
