package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.screen.BrewingStandMenuDelegate;
import net.errorcraft.itematic.screen.ItematicScreenHandlerTypes;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.GameType;

public class BrewingStandBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingWaterBottleWithNetherWartTurnsItIntoAwkwardPotion(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemKeys.NETHER_WART));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> Assert.itemStack(context, brewingStandMenu.getSlot(0).getItem())
                    .is(ItemKeys.POTION)
                    .hasPotion(Potions.AWKWARD)
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingAwkwardPotionWithSugarTurnsItIntoSwiftnessPotion(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.AWKWARD));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemKeys.SUGAR));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.SWIFTNESS);
                }
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingSwiftnessPotionWithGlowstoneDustTurnsItIntoStrongSwiftnessPotion(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemKeys.GLOWSTONE_DUST));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.STRONG_SWIFTNESS);
                }
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingSwiftnessPotionWithRedstoneTurnsItIntoLongSwiftnessPotion(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemKeys.REDSTONE));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.LONG_SWIFTNESS);
                }
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingSwiftnessPotionWithGunpowderTurnsItIntoSwiftnessSplashPotion(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemKeys.GUNPOWDER));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemKeys.SPLASH_POTION)
                        .hasPotion(Potions.SWIFTNESS);
                }
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingSwiftnessSplashPotionWithDragonBreathTurnsItIntoSwiftnessLingeringPotionAndLeavesGlassBottle(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.SPLASH_POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemKeys.DRAGON_BREATH));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemKeys.LINGERING_POTION)
                        .hasPotion(Potions.SWIFTNESS);
                    ItemStack ingredientRemainder = brewingStandMenu.getSlot(3).getItem();
                    Assert.itemStack(context, ingredientRemainder)
                        .is(ItemKeys.GLASS_BOTTLE);
                }
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingDifferentPotionsOnlyModifiesCorrectTargets(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(1)
            .setByPlayer(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER));
        brewingStandMenu.getSlot(2)
            .setByPlayer(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.LEAPING));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemKeys.NETHER_WART));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack firstPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, firstPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.SWIFTNESS);
                    ItemStack secondPotion = brewingStandMenu.getSlot(1).getItem();
                    Assert.itemStack(context, secondPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.AWKWARD);
                    ItemStack thirdPotion = brewingStandMenu.getSlot(2).getItem();
                    Assert.itemStack(context, thirdPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.LEAPING);
                }
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingPotionsTargetingMultipleValidRecipesModifiesBoth(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ItematicScreenHandlerTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER));
        brewingStandMenu.getSlot(1)
            .setByPlayer(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.AWKWARD));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemKeys.SUGAR));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack firstPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, firstPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.MUNDANE);
                    ItemStack secondPotion = brewingStandMenu.getSlot(1).getItem();
                    Assert.itemStack(context, secondPotion)
                        .is(ItemKeys.POTION)
                        .hasPotion(Potions.SWIFTNESS);
                }
            )
            .thenSucceed();
    }
}
