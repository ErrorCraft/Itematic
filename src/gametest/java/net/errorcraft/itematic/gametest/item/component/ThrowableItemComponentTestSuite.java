package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class ThrowableItemComponentTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 2, 1);

    @GameTest(templateName = "itematic:item.component.throwable.platform")
    public void throwingEggSpawnsEggAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.EGG);
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TypedActionResult<ItemStack> result = stack.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> {
            context.assertTrue(result.getResult().isAccepted(), "Expected Egg usage to be successful");
            context.expectEntityAt(EntityType.EGG, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        });
    }

    @GameTest(templateName = "itematic:item.component.throwable.platform")
    public void throwingEnderPearlSpawnsEnderPearlAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.ENDER_PEARL);
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TypedActionResult<ItemStack> result = stack.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> {
            context.assertTrue(result.getResult().isAccepted(), "Expected Ender Pearl usage to be successful");
            context.expectEntityAt(EntityType.ENDER_PEARL, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        });
    }

    @GameTest(templateName = "itematic:item.component.throwable.platform")
    public void throwingSnowballSpawnsSnowballAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SNOWBALL);
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TypedActionResult<ItemStack> result = stack.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> {
            context.assertTrue(result.getResult().isAccepted(), "Expected Snowball usage to be successful");
            context.expectEntityAt(EntityType.SNOWBALL, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        });
    }

    @GameTest(templateName = "itematic:item.component.throwable.platform")
    public void throwingExperienceBottleSpawnsExperienceBottleAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.EXPERIENCE_BOTTLE);
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TypedActionResult<ItemStack> result = stack.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> {
            context.assertTrue(result.getResult().isAccepted(), "Expected Experience Bottle usage to be successful");
            context.expectEntityAt(EntityType.EXPERIENCE_BOTTLE, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        });
    }

    @GameTest(templateName = "itematic:item.component.throwable.platform")
    public void throwingSplashPotionSpawnsPotionAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SPLASH_POTION);
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TypedActionResult<ItemStack> result = stack.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> {
            context.assertTrue(result.getResult().isAccepted(), "Expected Splash Potion usage to be successful");
            context.expectEntityAt(EntityType.POTION, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        });
    }

    @GameTest(templateName = "itematic:item.component.throwable.platform")
    public void throwingLingeringPotionSpawnsPotionAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.LINGERING_POTION);
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TypedActionResult<ItemStack> result = stack.use(world, player, Hand.MAIN_HAND);
        context.addFinalTask(() -> {
            context.assertTrue(result.getResult().isAccepted(), "Expected Lingering Potion usage to be successful");
            context.expectEntityAt(EntityType.POTION, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        });
    }

    @GameTest(templateName = "itematic:item.component.throwable.platform")
    public void throwingTridentSpawnsTridentAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.TRIDENT);
        int minDrawDuration = TestUtil.getItemComponent(stack, ItemComponentTypes.THROWABLE)
            .drawDuration()
            .flatMap(NumberRange.IntRange::min)
            .orElseThrow(() -> new GameTestException("Trident does not have a minimum draw duration"));
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TypedActionResult<ItemStack> result = stack.use(world, player, Hand.MAIN_HAND);
        context.assertTrue(result.getResult().isAccepted(), "Expected Trident usage to be successful");
        context.runAtTick(minDrawDuration, () -> context.addFinalTask(() -> {
            stack.onStoppedUsing(world, player, player.getItemUseTimeLeft());
            context.expectEntityAt(EntityType.TRIDENT, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        }));
    }
}
