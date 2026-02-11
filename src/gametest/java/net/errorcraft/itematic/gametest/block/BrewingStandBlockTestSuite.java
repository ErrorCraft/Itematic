package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class BrewingStandBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 2, 1);

    @GameTest(templateName = "itematic:block.brewing_stand", tickLimit = 401)
    public void brewingWaterBottleWithNetherWartTurnsItIntoAwkwardPotion(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandScreenHandler brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.BREWING_STAND);
        brewingStandMenu.getSlot(0)
            .setStack(PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER));
        brewingStandMenu.getSlot(3)
            .setStack(world.itematic$createStack(ItemKeys.NETHER_WART));
        brewingStandMenu.getSlot(4)
            .setStack(world.itematic$createStack(ItemKeys.BLAZE_POWDER));
        context.createTimedTaskRunner()
            .expectMinDurationAndRun(
                401,
                () -> {
                    ItemStack resultPotion = brewingStandMenu.getSlot(0).getStack();
                    Assert.itemStackIsOf(resultPotion, ItemKeys.POTION);
                    Assert.itemStackHasPotion(resultPotion, Potions.AWKWARD);
                }
            )
            .completeIfSuccessful();
    }

    @GameTest(templateName = "itematic:block.brewing_stand", tickLimit = 401)
    public void brewingAwkwardPotionWithSugarTurnsItIntoSwiftnessPotion(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandScreenHandler brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.BREWING_STAND);
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
                    Assert.itemStackIsOf(resultPotion, ItemKeys.POTION);
                    Assert.itemStackHasPotion(resultPotion, Potions.SWIFTNESS);
                }
            )
            .completeIfSuccessful();
    }

    @GameTest(templateName = "itematic:block.brewing_stand", tickLimit = 401)
    public void brewingSwiftnessPotionWithGlowstoneDustTurnsItIntoStrongSwiftnessPotion(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandScreenHandler brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.BREWING_STAND);
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
                    Assert.itemStackIsOf(resultPotion, ItemKeys.POTION);
                    Assert.itemStackHasPotion(resultPotion, Potions.STRONG_SWIFTNESS);
                }
            )
            .completeIfSuccessful();
    }

    @GameTest(templateName = "itematic:block.brewing_stand", tickLimit = 401)
    public void brewingSwiftnessPotionWithRedstoneTurnsItIntoLongSwiftnessPotion(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandScreenHandler brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.BREWING_STAND);
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
                    Assert.itemStackIsOf(resultPotion, ItemKeys.POTION);
                    Assert.itemStackHasPotion(resultPotion, Potions.LONG_SWIFTNESS);
                }
            )
            .completeIfSuccessful();
    }

    @GameTest(templateName = "itematic:block.brewing_stand", tickLimit = 401)
    public void brewingSwiftnessPotionWithGunpowderTurnsItIntoSwiftnessSplashPotion(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandScreenHandler brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.BREWING_STAND);
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
                    Assert.itemStackIsOf(resultPotion, ItemKeys.SPLASH_POTION);
                    Assert.itemStackHasPotion(resultPotion, Potions.SWIFTNESS);
                }
            )
            .completeIfSuccessful();
    }

    @GameTest(templateName = "itematic:block.brewing_stand", tickLimit = 401)
    public void brewingSwiftnessSplashPotionWithDragonBreathTurnsItIntoSwiftnessLingeringPotionAndLeavesGlassBottle(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        BrewingStandScreenHandler brewingStandMenu = TestUtil.getMenuFromBlock(context, BLOCK_POSITION, player, ScreenHandlerType.BREWING_STAND);
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
                    Assert.itemStackIsOf(resultPotion, ItemKeys.LINGERING_POTION);
                    Assert.itemStackHasPotion(resultPotion, Potions.SWIFTNESS);
                    ItemStack ingredientRemainder = brewingStandMenu.getSlot(3).getStack();
                    Assert.itemStackIsOf(ingredientRemainder, ItemKeys.GLASS_BOTTLE);
                }
            )
            .completeIfSuccessful();
    }
}