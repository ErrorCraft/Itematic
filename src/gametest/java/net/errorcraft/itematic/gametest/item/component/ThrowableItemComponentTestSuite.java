package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class ThrowableItemComponentTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingEggSpawnsEggAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack egg = world.itematic$createStack(ItemKeys.EGG);
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, egg);
        world.spawnEntity(player);
        context.addFinalTask(() -> {
            ActionResult result = egg.use(world, player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Egg usage to be successful"
            );
            context.expectEntityAt(EntityType.EGG, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingEnderPearlSpawnsEnderPearlAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack enderPearl = world.itematic$createStack(ItemKeys.ENDER_PEARL);
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, enderPearl);
        world.spawnEntity(player);
        context.addFinalTask(() -> {
            ActionResult result = enderPearl.use(world, player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Ender Pearl usage to be successful"
            );
            context.expectEntityAt(EntityType.ENDER_PEARL, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingSnowballSpawnsSnowballAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack snowball = world.itematic$createStack(ItemKeys.SNOWBALL);
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, snowball);
        world.spawnEntity(player);
        context.addFinalTask(() -> {
            ActionResult result = snowball.use(world, player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Snowball usage to be successful"
            );
            context.expectEntityAt(EntityType.SNOWBALL, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingExperienceBottleSpawnsExperienceBottleAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack experienceBottle = world.itematic$createStack(ItemKeys.EXPERIENCE_BOTTLE);
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, experienceBottle);
        world.spawnEntity(player);
        context.addFinalTask(() -> {
            ActionResult result = experienceBottle.use(world, player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Experience Bottle usage to be successful"
            );
            context.expectEntityAt(EntityType.EXPERIENCE_BOTTLE, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingSplashPotionSpawnsPotionAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack splashPotion = world.itematic$createStack(ItemKeys.SPLASH_POTION);
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, splashPotion);
        world.spawnEntity(player);
        context.addFinalTask(() -> {
            ActionResult result = splashPotion.use(world, player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Splash Potion usage to be successful"
            );
            context.expectEntityAt(EntityType.POTION, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingLingeringPotionSpawnsPotionAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack lingeringPotion = world.itematic$createStack(ItemKeys.LINGERING_POTION);
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, lingeringPotion);
        world.spawnEntity(player);
        context.addFinalTask(() -> {
            ActionResult result = lingeringPotion.use(world, player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Lingering Potion usage to be successful"
            );
            context.expectEntityAt(EntityType.POTION, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingTridentSpawnsTridentAtEyePosition(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack trident = world.itematic$createStack(ItemKeys.TRIDENT);
        int minDrawDuration = TestUtil.getItemBehavior(context, trident, ItemComponentTypes.THROWABLE)
            .drawDuration()
            .flatMap(NumberRange.IntRange::min)
            .orElseThrow(() -> context.createError(Text.literal("Trident does not have a minimum draw duration")));
        PlayerEntity player = TestUtil.createMockPlayer(context, GameMode.SURVIVAL, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, trident);
        world.spawnEntity(player);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> {
                ActionResult result = trident.use(world, player, Hand.MAIN_HAND);
                Assert.isTrue(
                    context,
                    result.isAccepted(),
                    () -> "Expected Trident usage to be successful"
                );
            })
            .expectMinDurationAndRun(minDrawDuration, () -> {
                trident.onStoppedUsing(world, player, player.getItemUseTimeLeft());
                context.expectEntityAt(EntityType.TRIDENT, SPAWN_POSITION.add(0, (int)player.getStandingEyeHeight(), 0));
            })
            .completeIfSuccessful();
    }
}
