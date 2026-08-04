package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class ThrowableItemComponentTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingEggSpawnsEggAtEyePosition(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack egg = world.itematic$createStack(ItemKeys.EGG);
        Player player = TestUtil.createMockPlayer(context, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, egg);
        world.addFreshEntity(player);
        context.succeedIf(() -> {
            InteractionResult result = egg.use(world, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.consumesAction(),
                () -> "Expected Egg usage to be successful"
            );
            context.assertEntityPresent(EntityType.EGG, SPAWN_POSITION.offset(0, (int)player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingEnderPearlSpawnsEnderPearlAtEyePosition(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack enderPearl = world.itematic$createStack(ItemKeys.ENDER_PEARL);
        Player player = TestUtil.createMockPlayer(context, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, enderPearl);
        world.addFreshEntity(player);
        context.succeedIf(() -> {
            InteractionResult result = enderPearl.use(world, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.consumesAction(),
                () -> "Expected Ender Pearl usage to be successful"
            );
            context.assertEntityPresent(EntityType.ENDER_PEARL, SPAWN_POSITION.offset(0, (int)player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingSnowballSpawnsSnowballAtEyePosition(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack snowball = world.itematic$createStack(ItemKeys.SNOWBALL);
        Player player = TestUtil.createMockPlayer(context, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, snowball);
        world.addFreshEntity(player);
        context.succeedIf(() -> {
            InteractionResult result = snowball.use(world, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.consumesAction(),
                () -> "Expected Snowball usage to be successful"
            );
            context.assertEntityPresent(EntityType.SNOWBALL, SPAWN_POSITION.offset(0, (int)player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingExperienceBottleSpawnsExperienceBottleAtEyePosition(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack experienceBottle = world.itematic$createStack(ItemKeys.EXPERIENCE_BOTTLE);
        Player player = TestUtil.createMockPlayer(context, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, experienceBottle);
        world.addFreshEntity(player);
        context.succeedIf(() -> {
            InteractionResult result = experienceBottle.use(world, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.consumesAction(),
                () -> "Expected Experience Bottle usage to be successful"
            );
            context.assertEntityPresent(EntityType.EXPERIENCE_BOTTLE, SPAWN_POSITION.offset(0, (int)player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingSplashPotionSpawnsPotionAtEyePosition(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack splashPotion = world.itematic$createStack(ItemKeys.SPLASH_POTION);
        Player player = TestUtil.createMockPlayer(context, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, splashPotion);
        world.addFreshEntity(player);
        context.succeedIf(() -> {
            InteractionResult result = splashPotion.use(world, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.consumesAction(),
                () -> "Expected Splash Potion usage to be successful"
            );
            context.assertEntityPresent(EntityType.SPLASH_POTION, SPAWN_POSITION.offset(0, (int)player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingLingeringPotionSpawnsPotionAtEyePosition(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack lingeringPotion = world.itematic$createStack(ItemKeys.LINGERING_POTION);
        Player player = TestUtil.createMockPlayer(context, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, lingeringPotion);
        world.addFreshEntity(player);
        context.succeedIf(() -> {
            InteractionResult result = lingeringPotion.use(world, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.consumesAction(),
                () -> "Expected Lingering Potion usage to be successful"
            );
            context.assertEntityPresent(EntityType.LINGERING_POTION, SPAWN_POSITION.offset(0, (int)player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingTridentSpawnsTridentAtEyePosition(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack trident = world.itematic$createStack(ItemKeys.TRIDENT);
        int minDrawDuration = TestUtil.getItemBehavior(context, trident, ItemComponentTypes.THROWABLE)
            .drawDuration()
            .flatMap(MinMaxBounds::min)
            .orElseThrow(() -> context.assertionException(Component.literal("Trident does not have a minimum draw duration")));
        Player player = TestUtil.createMockPlayer(context, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, trident);
        world.addFreshEntity(player);
        context.startSequence()
            .thenExecute(() -> {
                InteractionResult result = trident.use(world, player, InteractionHand.MAIN_HAND);
                Assert.isTrue(
                    context,
                    result.consumesAction(),
                    () -> "Expected Trident usage to be successful"
                );
            })
            .thenExecuteAfter(minDrawDuration, () -> {
                trident.releaseUsing(world, player, player.getUseItemRemainingTicks());
                context.assertEntityPresent(EntityType.TRIDENT, SPAWN_POSITION.offset(0, (int)player.getEyeHeight(), 0));
            })
            .thenSucceed();
    }
}
