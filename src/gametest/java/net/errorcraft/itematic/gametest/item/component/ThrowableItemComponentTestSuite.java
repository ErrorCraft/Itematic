package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
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
    public void throwingEggSpawnsEggAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack egg = level.itematic$createStack(ItemIds.EGG);
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, egg);
        level.addFreshEntity(player);
        helper.succeedIf(() -> {
            InteractionResult result = egg.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Egg usage to be successful"
            );
            helper.assertEntityPresent(EntityType.EGG, SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingEnderPearlSpawnsEnderPearlAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack enderPearl = level.itematic$createStack(ItemIds.ENDER_PEARL);
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, enderPearl);
        level.addFreshEntity(player);
        helper.succeedIf(() -> {
            InteractionResult result = enderPearl.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Ender Pearl usage to be successful"
            );
            helper.assertEntityPresent(EntityType.ENDER_PEARL, SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingSnowballSpawnsSnowballAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack snowball = level.itematic$createStack(ItemIds.SNOWBALL);
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, snowball);
        level.addFreshEntity(player);
        helper.succeedIf(() -> {
            InteractionResult result = snowball.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Snowball usage to be successful"
            );
            helper.assertEntityPresent(EntityType.SNOWBALL, SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingExperienceBottleSpawnsExperienceBottleAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack experienceBottle = level.itematic$createStack(ItemIds.EXPERIENCE_BOTTLE);
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, experienceBottle);
        level.addFreshEntity(player);
        helper.succeedIf(() -> {
            InteractionResult result = experienceBottle.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Experience Bottle usage to be successful"
            );
            helper.assertEntityPresent(EntityType.EXPERIENCE_BOTTLE, SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingSplashPotionSpawnsPotionAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack splashPotion = level.itematic$createStack(ItemIds.SPLASH_POTION);
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, splashPotion);
        level.addFreshEntity(player);
        helper.succeedIf(() -> {
            InteractionResult result = splashPotion.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Splash Potion usage to be successful"
            );
            helper.assertEntityPresent(EntityType.SPLASH_POTION, SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingLingeringPotionSpawnsPotionAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack lingeringPotion = level.itematic$createStack(ItemIds.LINGERING_POTION);
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, lingeringPotion);
        level.addFreshEntity(player);
        helper.succeedIf(() -> {
            InteractionResult result = lingeringPotion.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Lingering Potion usage to be successful"
            );
            helper.assertEntityPresent(EntityType.LINGERING_POTION, SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingTridentSpawnsTridentAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack trident = level.itematic$createStack(ItemIds.TRIDENT);
        int minDrawDuration = TestUtil.getItemBehavior(helper, trident, ItemBehaviorType.THROWABLE)
            .drawDuration()
            .flatMap(MinMaxBounds::min)
            .orElseThrow(() -> helper.assertionException(Component.literal("Trident does not have a minimum draw duration")));
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, trident);
        level.addFreshEntity(player);
        helper.startSequence()
            .thenExecute(() -> {
                InteractionResult result = trident.use(level, player, InteractionHand.MAIN_HAND);
                Assert.isTrue(
                    helper,
                    result.consumesAction(),
                    () -> "Expected Trident usage to be successful"
                );
            })
            .thenExecuteAfter(minDrawDuration, () -> {
                trident.releaseUsing(level, player, player.getUseItemRemainingTicks());
                helper.assertEntityPresent(EntityType.TRIDENT, SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
            })
            .thenSucceed();
    }
}
