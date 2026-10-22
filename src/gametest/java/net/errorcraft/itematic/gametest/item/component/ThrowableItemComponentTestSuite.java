package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
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
        helper.succeedIf(() -> {
            InteractionResult result = egg.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Egg usage to be successful"
            );
            Assert.entityType(helper, EntityTypes.EGG)
                .existsAt(SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingEnderPearlSpawnsEnderPearlAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack enderPearl = level.itematic$createStack(ItemIds.ENDER_PEARL);
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, enderPearl);
        helper.succeedIf(() -> {
            InteractionResult result = enderPearl.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Ender Pearl usage to be successful"
            );
            Assert.entityType(helper, EntityTypes.ENDER_PEARL)
                .existsAt(SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingSnowballSpawnsSnowballAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack snowball = level.itematic$createStack(ItemIds.SNOWBALL);
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, snowball);
        helper.succeedIf(() -> {
            InteractionResult result = snowball.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Snowball usage to be successful"
            );
            Assert.entityType(helper, EntityTypes.SNOWBALL)
                .existsAt(SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingExperienceBottleSpawnsExperienceBottleAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack experienceBottle = level.itematic$createStack(ItemIds.EXPERIENCE_BOTTLE);
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, experienceBottle);
        helper.succeedIf(() -> {
            InteractionResult result = experienceBottle.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Experience Bottle usage to be successful"
            );
            Assert.entityType(helper, EntityTypes.EXPERIENCE_BOTTLE)
                .existsAt(SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingSplashPotionSpawnsPotionAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack splashPotion = level.itematic$createStack(ItemIds.SPLASH_POTION);
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, splashPotion);
        helper.succeedIf(() -> {
            InteractionResult result = splashPotion.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Splash Potion usage to be successful"
            );
            Assert.entityType(helper, EntityTypes.SPLASH_POTION)
                .existsAt(SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingLingeringPotionSpawnsPotionAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack lingeringPotion = level.itematic$createStack(ItemIds.LINGERING_POTION);
        Player player = TestUtil.createMockPlayer(helper, GameType.SURVIVAL, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, lingeringPotion);
        helper.succeedIf(() -> {
            InteractionResult result = lingeringPotion.use(level, player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Lingering Potion usage to be successful"
            );
            Assert.entityType(helper, EntityTypes.LINGERING_POTION)
                .existsAt(SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
        });
    }

    @GameTest(structure = "itematic:item.component.throwable.platform")
    public void throwingTridentSpawnsTridentAtEyePosition(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack trident = level.itematic$createStack(ItemIds.TRIDENT);
        int minDrawDuration = TestUtil.getDataComponent(helper, trident, ItematicDataComponents.THROWABLE)
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
                Assert.entityType(helper, EntityTypes.TRIDENT)
                    .existsAt(SPAWN_POSITION.offset(0, (int) player.getEyeHeight(), 0));
            })
            .thenSucceed();
    }
}
