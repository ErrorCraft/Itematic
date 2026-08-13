package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.screen.BrewingStandMenuDelegate;
import net.errorcraft.itematic.screen.ItematicScreenHandlerTypes;
import net.errorcraft.itematic.util.TestUtil;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
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
            .setByPlayer(PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.POTION), Potions.WATER));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemIds.NETHER_WART));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemIds.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> Assert.itemStack(context, brewingStandMenu.getSlot(0).getItem())
                    .is(ItemIds.POTION)
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
            .setByPlayer(PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.POTION), Potions.AWKWARD));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemIds.SUGAR));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemIds.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemIds.POTION)
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
            .setByPlayer(PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemIds.GLOWSTONE_DUST));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemIds.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemIds.POTION)
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
            .setByPlayer(PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemIds.REDSTONE));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemIds.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemIds.POTION)
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
            .setByPlayer(PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemIds.GUNPOWDER));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemIds.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemIds.SPLASH_POTION)
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
            .setByPlayer(PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.SPLASH_POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemIds.DRAGON_BREATH));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemIds.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, resultPotion)
                        .is(ItemIds.LINGERING_POTION)
                        .hasPotion(Potions.SWIFTNESS);
                    ItemStack ingredientRemainder = brewingStandMenu.getSlot(3).getItem();
                    Assert.itemStack(context, ingredientRemainder)
                        .is(ItemIds.GLASS_BOTTLE);
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
            .setByPlayer(PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(1)
            .setByPlayer(PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.POTION), Potions.WATER));
        brewingStandMenu.getSlot(2)
            .setByPlayer(PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.POTION), Potions.LEAPING));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemIds.NETHER_WART));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemIds.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack firstPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, firstPotion)
                        .is(ItemIds.POTION)
                        .hasPotion(Potions.SWIFTNESS);
                    ItemStack secondPotion = brewingStandMenu.getSlot(1).getItem();
                    Assert.itemStack(context, secondPotion)
                        .is(ItemIds.POTION)
                        .hasPotion(Potions.AWKWARD);
                    ItemStack thirdPotion = brewingStandMenu.getSlot(2).getItem();
                    Assert.itemStack(context, thirdPotion)
                        .is(ItemIds.POTION)
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
            .setByPlayer(PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.POTION), Potions.WATER));
        brewingStandMenu.getSlot(1)
            .setByPlayer(PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.POTION), Potions.AWKWARD));
        brewingStandMenu.getSlot(3)
            .setByPlayer(world.itematic$createStack(ItemIds.SUGAR));
        brewingStandMenu.getSlot(4)
            .setByPlayer(world.itematic$createStack(ItemIds.BLAZE_POWDER));
        context.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack firstPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(context, firstPotion)
                        .is(ItemIds.POTION)
                        .hasPotion(Potions.MUNDANE);
                    ItemStack secondPotion = brewingStandMenu.getSlot(1).getItem();
                    Assert.itemStack(context, secondPotion)
                        .is(ItemIds.POTION)
                        .hasPotion(Potions.SWIFTNESS);
                }
            )
            .thenSucceed();
    }
}
