package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class BrushTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(2, 1, 2);

    @GameTest(templateName = "itematic:item.brush.platform")
    public void usingBrushDoesNotStartBrushing(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BRUSH);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        stack.use(world, player, Hand.MAIN_HAND);
        context.addInstantFinalTask(() -> context.assertFalse(player.isUsingItem(), "Expected player to not have started using a Brush"));
    }

    @GameTest(templateName = "itematic:item.brush.platform.suspicious_sand")
    public void usingBrushOnBlockStartsBrushing(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BRUSH);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> context.assertTrue(player.isUsingItem(), "Expected player to have started using a Brush"));
    }

    @GameTest(templateName = "itematic:item.brush.platform.short_grass")
    public void usingBrushOnIntangibleBlockDoesNotStartBrushing(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BRUSH);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> context.assertFalse(player.isUsingItem(), "Expected player to not have started using a Brush"));
    }
}
