package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.screen.BrewingStandMenuDelegate;
import net.errorcraft.itematic.screen.ItematicScreenHandlerTypes;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class BrewingStandBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingWaterBottleWithNetherWartTurnsItIntoAwkwardPotion(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setStack(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER));
        brewingStandMenu.getSlot(3)
            .setStack(world.itematic$createStack(ItemKeys.NETHER_WART));
        brewingStandMenu.getSlot(4)
            .setStack(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.createTimedTaskRunner()
            .expectMinDurationAndRun(
                401,
                () -> Assert.itemStack(context, brewingStandMenu.getSlot(0).getStack())
                    .is(ItemKeys.POTION)
                    .hasPotion(Potions.AWKWARD)
            )
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingAwkwardPotionWithSugarTurnsItIntoSwiftnessPotion(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setStack(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.AWKWARD));
        brewingStandMenu.getSlot(3)
            .setStack(world.itematic$createStack(ItemKeys.SUGAR));
        brewingStandMenu.getSlot(4)
            .setStack(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.createTimedTaskRunner()
            .expectMinDurationAndRun(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getStack();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.SWIFTNESS);
                }
            )
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingSwiftnessPotionWithGlowstoneDustTurnsItIntoStrongSwiftnessPotion(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setStack(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setStack(world.itematic$createStack(ItemKeys.GLOWSTONE_DUST));
        brewingStandMenu.getSlot(4)
            .setStack(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.createTimedTaskRunner()
            .expectMinDurationAndRun(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getStack();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.STRONG_SWIFTNESS);
                }
            )
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingSwiftnessPotionWithRedstoneTurnsItIntoLongSwiftnessPotion(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setStack(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setStack(world.itematic$createStack(ItemKeys.REDSTONE));
        brewingStandMenu.getSlot(4)
            .setStack(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.createTimedTaskRunner()
            .expectMinDurationAndRun(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getStack();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.LONG_SWIFTNESS);
                }
            )
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingSwiftnessPotionWithGunpowderTurnsItIntoSwiftnessSplashPotion(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setStack(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setStack(world.itematic$createStack(ItemKeys.GUNPOWDER));
        brewingStandMenu.getSlot(4)
            .setStack(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.createTimedTaskRunner()
            .expectMinDurationAndRun(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getStack();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemKeys.SPLASH_POTION)
                        .hasPotion(Potions.SWIFTNESS);
                }
            )
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingSwiftnessSplashPotionWithDragonBreathTurnsItIntoSwiftnessLingeringPotionAndLeavesGlassBottle(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setStack(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.SPLASH_POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setStack(world.itematic$createStack(ItemKeys.DRAGON_BREATH));
        brewingStandMenu.getSlot(4)
            .setStack(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.createTimedTaskRunner()
            .expectMinDurationAndRun(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getStack();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemKeys.LINGERING_POTION)
                        .hasPotion(Potions.SWIFTNESS);
                    ItemStack ingredientRemainder = brewingStandMenu.getSlot(3).getStack();
                    Assert.itemStack(context, ingredientRemainder)
                        .is(ItemKeys.GLASS_BOTTLE);
                }
            )
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingDifferentPotionsOnlyModifiesCorrectTargets(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setStack(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(1)
            .setStack(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER));
        brewingStandMenu.getSlot(2)
            .setStack(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.LEAPING));
        brewingStandMenu.getSlot(3)
            .setStack(world.itematic$createStack(ItemKeys.NETHER_WART));
        brewingStandMenu.getSlot(4)
            .setStack(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.createTimedTaskRunner()
            .expectMinDurationAndRun(
                401,
                () -> {
                    ItemStack firstPotion = brewingStandMenu.getSlot(0).getStack();
                    Assert.itemStack(context, firstPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.SWIFTNESS);
                    ItemStack secondPotion = brewingStandMenu.getSlot(1).getStack();
                    Assert.itemStack(context, secondPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.AWKWARD);
                    ItemStack thirdPotion = brewingStandMenu.getSlot(2).getStack();
                    Assert.itemStack(context, thirdPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.LEAPING);
                }
            )
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingPotionsTargetingMultipleValidRecipesModifiesBoth(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setStack(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER));
        brewingStandMenu.getSlot(1)
            .setStack(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.AWKWARD));
        brewingStandMenu.getSlot(3)
            .setStack(world.itematic$createStack(ItemKeys.SUGAR));
        brewingStandMenu.getSlot(4)
            .setStack(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.createTimedTaskRunner()
            .expectMinDurationAndRun(
                401,
                () -> {
                    ItemStack firstPotion = brewingStandMenu.getSlot(0).getStack();
                    Assert.itemStack(context, firstPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.MUNDANE);
                    ItemStack secondPotion = brewingStandMenu.getSlot(1).getStack();
                    Assert.itemStack(context, secondPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.SWIFTNESS);
                }
            )
            .completeIfSuccessful();
    }
}
