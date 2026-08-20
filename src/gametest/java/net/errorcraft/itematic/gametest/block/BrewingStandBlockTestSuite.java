package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.errorcraft.itematic.world.inventory.BrewingStandMenuDelegate;
import net.errorcraft.itematic.world.inventory.ItematicMenuTypes;
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
    public void brewingWaterBottleWithNetherWartTurnsItIntoAwkwardPotion(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, ItematicMenuTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.POTION), Potions.WATER));
        brewingStandMenu.getSlot(3)
            .setByPlayer(level.itematic$createStack(ItemIds.NETHER_WART));
        brewingStandMenu.getSlot(4)
            .setByPlayer(level.itematic$createStack(ItemIds.BLAZE_POWDER));
        helper.startSequence()
            .thenExecuteAfter(
                401,
                () -> Assert.itemStack(helper, brewingStandMenu.getSlot(0).getItem())
                    .is(ItemIds.POTION)
                    .hasPotion(Potions.AWKWARD)
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingAwkwardPotionWithSugarTurnsItIntoSwiftnessPotion(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, ItematicMenuTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.POTION), Potions.AWKWARD));
        brewingStandMenu.getSlot(3)
            .setByPlayer(level.itematic$createStack(ItemIds.SUGAR));
        brewingStandMenu.getSlot(4)
            .setByPlayer(level.itematic$createStack(ItemIds.BLAZE_POWDER));
        helper.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(helper, resultPotion)
                        .is(ItemIds.POTION)
                        .hasPotion(Potions.SWIFTNESS);
                }
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingSwiftnessPotionWithGlowstoneDustTurnsItIntoStrongSwiftnessPotion(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, ItematicMenuTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setByPlayer(level.itematic$createStack(ItemIds.GLOWSTONE_DUST));
        brewingStandMenu.getSlot(4)
            .setByPlayer(level.itematic$createStack(ItemIds.BLAZE_POWDER));
        helper.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(helper, resultPotion)
                        .is(ItemIds.POTION)
                        .hasPotion(Potions.STRONG_SWIFTNESS);
                }
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingSwiftnessPotionWithRedstoneTurnsItIntoLongSwiftnessPotion(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, ItematicMenuTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setByPlayer(level.itematic$createStack(ItemIds.REDSTONE));
        brewingStandMenu.getSlot(4)
            .setByPlayer(level.itematic$createStack(ItemIds.BLAZE_POWDER));
        helper.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(helper, resultPotion)
                        .is(ItemIds.POTION)
                        .hasPotion(Potions.LONG_SWIFTNESS);
                }
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingSwiftnessPotionWithGunpowderTurnsItIntoSwiftnessSplashPotion(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, ItematicMenuTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setByPlayer(level.itematic$createStack(ItemIds.GUNPOWDER));
        brewingStandMenu.getSlot(4)
            .setByPlayer(level.itematic$createStack(ItemIds.BLAZE_POWDER));
        helper.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(helper, resultPotion)
                        .is(ItemIds.SPLASH_POTION)
                        .hasPotion(Potions.SWIFTNESS);
                }
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingSwiftnessSplashPotionWithDragonBreathTurnsItIntoSwiftnessLingeringPotionAndLeavesGlassBottle(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, ItematicMenuTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.SPLASH_POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(3)
            .setByPlayer(level.itematic$createStack(ItemIds.DRAGON_BREATH));
        brewingStandMenu.getSlot(4)
            .setByPlayer(level.itematic$createStack(ItemIds.BLAZE_POWDER));
        helper.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(helper, resultPotion)
                        .is(ItemIds.LINGERING_POTION)
                        .hasPotion(Potions.SWIFTNESS);
                    ItemStack ingredientRemainder = brewingStandMenu.getSlot(3).getItem();
                    Assert.itemStack(helper, ingredientRemainder)
                        .is(ItemIds.GLASS_BOTTLE);
                }
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingDifferentPotionsOnlyModifiesCorrectTargets(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, ItematicMenuTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.POTION), Potions.SWIFTNESS));
        brewingStandMenu.getSlot(1)
            .setByPlayer(PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.POTION), Potions.WATER));
        brewingStandMenu.getSlot(2)
            .setByPlayer(PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.POTION), Potions.LEAPING));
        brewingStandMenu.getSlot(3)
            .setByPlayer(level.itematic$createStack(ItemIds.NETHER_WART));
        brewingStandMenu.getSlot(4)
            .setByPlayer(level.itematic$createStack(ItemIds.BLAZE_POWDER));
        helper.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack firstPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(helper, firstPotion)
                        .is(ItemIds.POTION)
                        .hasPotion(Potions.SWIFTNESS);
                    ItemStack secondPotion = brewingStandMenu.getSlot(1).getItem();
                    Assert.itemStack(helper, secondPotion)
                        .is(ItemIds.POTION)
                        .hasPotion(Potions.AWKWARD);
                    ItemStack thirdPotion = brewingStandMenu.getSlot(2).getItem();
                    Assert.itemStack(helper, thirdPotion)
                        .is(ItemIds.POTION)
                        .hasPotion(Potions.LEAPING);
                }
            )
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.brewing_stand", maxTicks = 401)
    public void brewingPotionsTargetingMultipleValidRecipesModifiesBoth(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        BrewingStandMenuDelegate brewingStandMenu = TestUtil.getMenuFromBlock(helper, BLOCK_POSITION, player, ItematicMenuTypes.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setByPlayer(PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.POTION), Potions.WATER));
        brewingStandMenu.getSlot(1)
            .setByPlayer(PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.POTION), Potions.AWKWARD));
        brewingStandMenu.getSlot(3)
            .setByPlayer(level.itematic$createStack(ItemIds.SUGAR));
        brewingStandMenu.getSlot(4)
            .setByPlayer(level.itematic$createStack(ItemIds.BLAZE_POWDER));
        helper.startSequence()
            .thenExecuteAfter(
                401,
                () -> {
                    ItemStack firstPotion = brewingStandMenu.getSlot(0).getItem();
                    Assert.itemStack(helper, firstPotion)
                        .is(ItemIds.POTION)
                        .hasPotion(Potions.MUNDANE);
                    ItemStack secondPotion = brewingStandMenu.getSlot(1).getItem();
                    Assert.itemStack(helper, secondPotion)
                        .is(ItemIds.POTION)
                        .hasPotion(Potions.SWIFTNESS);
                }
            )
            .thenSucceed();
    }
}
