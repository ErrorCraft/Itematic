package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

public class FlowerPotItemTestSuite {
    private static final BlockPos FLOWER_POT_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOakSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack oakSapling = level.itematic$createStack(ItemIds.OAK_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oakSapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, oakSapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, oakSapling)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_OAK_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOakSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack oakSapling = level.itematic$createStack(ItemIds.OAK_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oakSapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, oakSapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.OAK_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingSpruceSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack spruceSapling = level.itematic$createStack(ItemIds.SPRUCE_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, spruceSapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, spruceSapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, spruceSapling)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_SPRUCE_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingSpruceSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack spruceSapling = level.itematic$createStack(ItemIds.SPRUCE_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, spruceSapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, spruceSapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.SPRUCE_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBirchSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack birchSapling = level.itematic$createStack(ItemIds.BIRCH_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, birchSapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, birchSapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, birchSapling)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BIRCH_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBirchSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack birchSapling = level.itematic$createStack(ItemIds.BIRCH_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, birchSapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, birchSapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.BIRCH_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingJungleSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack jungleSapling = level.itematic$createStack(ItemIds.JUNGLE_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, jungleSapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, jungleSapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, jungleSapling)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_JUNGLE_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingJungleSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack jungleSapling = level.itematic$createStack(ItemIds.JUNGLE_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, jungleSapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, jungleSapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.JUNGLE_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAcaciaSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack acaciaSapling = level.itematic$createStack(ItemIds.ACACIA_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, acaciaSapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, acaciaSapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, acaciaSapling)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_ACACIA_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAcaciaSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack acaciaSapling = level.itematic$createStack(ItemIds.ACACIA_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, acaciaSapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, acaciaSapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.ACACIA_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCherrySaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack cherrySapling = level.itematic$createStack(ItemIds.CHERRY_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, cherrySapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, cherrySapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, cherrySapling)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CHERRY_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCherrySaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack cherrySapling = level.itematic$createStack(ItemIds.CHERRY_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, cherrySapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, cherrySapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.CHERRY_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDarkOakSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack darkOakSapling = level.itematic$createStack(ItemIds.DARK_OAK_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, darkOakSapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, darkOakSapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, darkOakSapling)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DARK_OAK_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDarkOakSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack darkOakSapling = level.itematic$createStack(ItemIds.DARK_OAK_SAPLING);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, darkOakSapling);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, darkOakSapling, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.DARK_OAK_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCrimsonFungusOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack crimsonFungus = level.itematic$createStack(ItemIds.CRIMSON_FUNGUS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, crimsonFungus);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, crimsonFungus, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, crimsonFungus)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CRIMSON_FUNGUS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCrimsonFungusOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack crimsonFungus = level.itematic$createStack(ItemIds.CRIMSON_FUNGUS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, crimsonFungus);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, crimsonFungus, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.CRIMSON_FUNGUS);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWarpedFungusOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack warpedFungus = level.itematic$createStack(ItemIds.WARPED_FUNGUS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, warpedFungus);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, warpedFungus, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, warpedFungus)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WARPED_FUNGUS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWarpedFungusOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack warpedFungus = level.itematic$createStack(ItemIds.WARPED_FUNGUS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, warpedFungus);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, warpedFungus, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.WARPED_FUNGUS);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCrimsonRootsOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack crimsonRoots = level.itematic$createStack(ItemIds.CRIMSON_ROOTS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, crimsonRoots);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, crimsonRoots, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, crimsonRoots)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CRIMSON_ROOTS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCrimsonRootsOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack crimsonRoots = level.itematic$createStack(ItemIds.CRIMSON_ROOTS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, crimsonRoots);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, crimsonRoots, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.CRIMSON_ROOTS);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWarpedRootsOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack warpedRoots = level.itematic$createStack(ItemIds.WARPED_ROOTS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, warpedRoots);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, warpedRoots, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, warpedRoots)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WARPED_ROOTS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWarpedRootsOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack warpedRoots = level.itematic$createStack(ItemIds.WARPED_ROOTS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, warpedRoots);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, warpedRoots, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.WARPED_ROOTS);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAzaleaBushOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack azalea = level.itematic$createStack(ItemIds.AZALEA);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, azalea);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, azalea, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, azalea)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_AZALEA);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAzaleaBushOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack azalea = level.itematic$createStack(ItemIds.AZALEA);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, azalea);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, azalea, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.AZALEA);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingFloweringAzaleaBushOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack floweringAzalea = level.itematic$createStack(ItemIds.FLOWERING_AZALEA);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, floweringAzalea);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, floweringAzalea, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, floweringAzalea)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_FLOWERING_AZALEA);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingFloweringAzaleaBushOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack floweringAzalea = level.itematic$createStack(ItemIds.FLOWERING_AZALEA);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, floweringAzalea);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, floweringAzalea, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.FLOWERING_AZALEA);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingMangrovePropaguleOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack mangrovePropagule = level.itematic$createStack(ItemIds.MANGROVE_PROPAGULE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, mangrovePropagule);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, mangrovePropagule, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, mangrovePropagule)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_MANGROVE_PROPAGULE);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingMangrovePropaguleOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack mangrovePropagule = level.itematic$createStack(ItemIds.MANGROVE_PROPAGULE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, mangrovePropagule);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, mangrovePropagule, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.MANGROVE_PROPAGULE);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCactusOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack cactus = level.itematic$createStack(ItemIds.CACTUS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, cactus);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, cactus, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, cactus)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CACTUS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCactusOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack cactus = level.itematic$createStack(ItemIds.CACTUS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, cactus);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, cactus, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.CACTUS);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDeadBushOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack deadBush = level.itematic$createStack(ItemIds.DEAD_BUSH);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, deadBush);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, deadBush, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, deadBush)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DEAD_BUSH);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDeadBushOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack deadBush = level.itematic$createStack(ItemIds.DEAD_BUSH);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, deadBush);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, deadBush, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.DEAD_BUSH);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBambooOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack bamboo = level.itematic$createStack(ItemIds.BAMBOO);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, bamboo);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, bamboo, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, bamboo)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BAMBOO);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBambooOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack bamboo = level.itematic$createStack(ItemIds.BAMBOO);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, bamboo);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, bamboo, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.BAMBOO);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingFernOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack fern = level.itematic$createStack(ItemIds.FERN);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, fern);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, fern, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, fern)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_FERN);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingFernOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack fern = level.itematic$createStack(ItemIds.FERN);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, fern);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, fern, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.FERN);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDandelionOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack dandelion = level.itematic$createStack(ItemIds.DANDELION);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, dandelion);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, dandelion, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, dandelion)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DANDELION);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDandelionOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack dandelion = level.itematic$createStack(ItemIds.DANDELION);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, dandelion);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, dandelion, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.DANDELION);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingPoppyOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack poppy = level.itematic$createStack(ItemIds.POPPY);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, poppy);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, poppy, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, poppy)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dandelion")
    public void usingPoppyOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack poppy = level.itematic$createStack(ItemIds.POPPY);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, poppy);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, poppy, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.POPPY);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DANDELION);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBlueOrchidOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack blueOrchid = level.itematic$createStack(ItemIds.BLUE_ORCHID);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, blueOrchid);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, blueOrchid, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, blueOrchid)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BLUE_ORCHID);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBlueOrchidOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack blueOrchid = level.itematic$createStack(ItemIds.BLUE_ORCHID);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, blueOrchid);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, blueOrchid, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.BLUE_ORCHID);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAlliumOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack allium = level.itematic$createStack(ItemIds.ALLIUM);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, allium);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, allium, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, allium)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_ALLIUM);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAlliumOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack allium = level.itematic$createStack(ItemIds.ALLIUM);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, allium);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, allium, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.ALLIUM);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAzureBluetOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack azureBluet = level.itematic$createStack(ItemIds.AZURE_BLUET);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, azureBluet);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, azureBluet, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, azureBluet)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_AZURE_BLUET);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAzureBluetOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack azureBluet = level.itematic$createStack(ItemIds.AZURE_BLUET);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, azureBluet);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, azureBluet, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.AZURE_BLUET);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingRedTulipOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack redTulip = level.itematic$createStack(ItemIds.RED_TULIP);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, redTulip);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, redTulip, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, redTulip)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_RED_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingRedTulipOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack redTulip = level.itematic$createStack(ItemIds.RED_TULIP);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, redTulip);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, redTulip, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.RED_TULIP);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOrangeTulipOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack orangeTulip = level.itematic$createStack(ItemIds.ORANGE_TULIP);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, orangeTulip);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, orangeTulip, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, orangeTulip)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_ORANGE_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOrangeTulipOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack orangeTulip = level.itematic$createStack(ItemIds.ORANGE_TULIP);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, orangeTulip);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, orangeTulip, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.ORANGE_TULIP);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWhiteTulipOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack whiteTulip = level.itematic$createStack(ItemIds.WHITE_TULIP);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, whiteTulip);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, whiteTulip, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, whiteTulip)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WHITE_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWhiteTulipOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack whiteTulip = level.itematic$createStack(ItemIds.WHITE_TULIP);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, whiteTulip);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, whiteTulip, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.WHITE_TULIP);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingPinkTulipOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack pinkTulip = level.itematic$createStack(ItemIds.PINK_TULIP);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, pinkTulip);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, pinkTulip, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, pinkTulip)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_PINK_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingPinkTulipOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack pinkTulip = level.itematic$createStack(ItemIds.PINK_TULIP);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, pinkTulip);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, pinkTulip, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.PINK_TULIP);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOxeyeDaisyOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack oxeyeDaisy = level.itematic$createStack(ItemIds.OXEYE_DAISY);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oxeyeDaisy);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, oxeyeDaisy, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, oxeyeDaisy)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_OXEYE_DAISY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOxeyeDaisyOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack oxeyeDaisy = level.itematic$createStack(ItemIds.OXEYE_DAISY);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oxeyeDaisy);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, oxeyeDaisy, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.OXEYE_DAISY);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCornflowerOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack cornflower = level.itematic$createStack(ItemIds.CORNFLOWER);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, cornflower);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, cornflower, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, cornflower)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CORNFLOWER);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCornflowerOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack cornflower = level.itematic$createStack(ItemIds.CORNFLOWER);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, cornflower);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, cornflower, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.CORNFLOWER);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingLilyOfTheValleyOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack lilyOfTheValley = level.itematic$createStack(ItemIds.LILY_OF_THE_VALLEY);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, lilyOfTheValley);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, lilyOfTheValley, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, lilyOfTheValley)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_LILY_OF_THE_VALLEY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingLilyOfTheValleyOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack lilyOfTheValley = level.itematic$createStack(ItemIds.LILY_OF_THE_VALLEY);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, lilyOfTheValley);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, lilyOfTheValley, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.LILY_OF_THE_VALLEY);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWitherRoseOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack witherRose = level.itematic$createStack(ItemIds.WITHER_ROSE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, witherRose);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, witherRose, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, witherRose)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WITHER_ROSE);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWitherRoseOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack witherRose = level.itematic$createStack(ItemIds.WITHER_ROSE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, witherRose);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, witherRose, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.WITHER_ROSE);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingTorchflowerOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack torchflower = level.itematic$createStack(ItemIds.TORCHFLOWER);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, torchflower);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, torchflower, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, torchflower)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_TORCHFLOWER);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingTorchflowerOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack torchflower = level.itematic$createStack(ItemIds.TORCHFLOWER);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, torchflower);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, torchflower, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.TORCHFLOWER);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingRedMushroomOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack redMushroom = level.itematic$createStack(ItemIds.RED_MUSHROOM);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, redMushroom);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, redMushroom, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, redMushroom)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_RED_MUSHROOM);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingRedMushroomOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack redMushroom = level.itematic$createStack(ItemIds.RED_MUSHROOM);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, redMushroom);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, redMushroom, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.RED_MUSHROOM);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBrownMushroomOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack brownMushroom = level.itematic$createStack(ItemIds.BROWN_MUSHROOM);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, brownMushroom);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, brownMushroom, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, brownMushroom)
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BROWN_MUSHROOM);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBrownMushroomOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack brownMushroom = level.itematic$createStack(ItemIds.BROWN_MUSHROOM);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, brownMushroom);
        level.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(helper, player, brownMushroom, FLOWER_POT_POSITION, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.BROWN_MUSHROOM);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_oak_sapling")
    public void usingHandOnPottedOakSaplingEmptiesPottedOakSaplingAndGivesOakSapling(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.OAK_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_spruce_sapling")
    public void usingHandOnPottedSpruceSaplingEmptiesPottedSpruceSaplingAndGivesSpruceSapling(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.SPRUCE_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_birch_sapling")
    public void usingHandOnPottedBirchSaplingEmptiesPottedBirchSaplingAndGivesBirchSapling(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.BIRCH_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_jungle_sapling")
    public void usingHandOnPottedJungleSaplingEmptiesPottedJungleSaplingAndGivesJungleSapling(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.JUNGLE_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_acacia_sapling")
    public void usingHandOnPottedAcaciaSaplingEmptiesPottedAcaciaSaplingAndGivesAcaciaSapling(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.ACACIA_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_cherry_sapling")
    public void usingHandOnPottedCherrySaplingEmptiesPottedCherrySaplingAndGivesCherrySapling(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.CHERRY_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dark_oak_sapling")
    public void usingHandOnPottedDarkOakSaplingEmptiesPottedDarkOakSaplingAndGivesDarkOakSapling(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.DARK_OAK_SAPLING);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_crimson_fungus")
    public void usingHandOnPottedCrimsonFungusEmptiesPottedCrimsonFungusAndGivesCrimsonFungus(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.CRIMSON_FUNGUS);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_warped_fungus")
    public void usingHandOnPottedWarpedFungusEmptiesPottedWarpedFungusAndGivesWarpedFungus(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.WARPED_FUNGUS);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_crimson_roots")
    public void usingHandOnPottedCrimsonRootsEmptiesPottedCrimsonRootsAndGivesCrimsonRoots(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.CRIMSON_ROOTS);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_warped_roots")
    public void usingHandOnPottedWarpedRootsEmptiesPottedWarpedRootsAndGivesWarpedRoots(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.WARPED_ROOTS);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_azalea_bush")
    public void usingHandOnPottedAzaleaEmptiesPottedAzaleaAndGivesAzalea(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.AZALEA);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_flowering_azalea_bush")
    public void usingHandOnPottedFloweringAzaleaEmptiesPottedFloweringAzaleaAndGivesFloweringAzalea(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.FLOWERING_AZALEA);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_mangrove_propagule")
    public void usingHandOnPottedMangrovePropaguleEmptiesPottedMangrovePropaguleAndGivesMangrovePropagule(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.MANGROVE_PROPAGULE);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_cactus")
    public void usingHandOnPottedCactusEmptiesPottedCactusAndGivesCactus(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.CACTUS);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dead_bush")
    public void usingHandOnPottedDeadBushEmptiesPottedDeadBushAndGivesDeadBush(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.DEAD_BUSH);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_bamboo")
    public void usingHandOnPottedBambooEmptiesPottedBambooAndGivesBamboo(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.BAMBOO);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_fern")
    public void usingHandOnPottedFernEmptiesPottedFernAndGivesFern(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.FERN);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dandelion")
    public void usingHandOnPottedDandelionEmptiesPottedDandelionAndGivesDandelion(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.DANDELION);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingHandOnPottedPoppyEmptiesPottedPoppyAndGivesPoppy(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.POPPY);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_blue_orchid")
    public void usingHandOnPottedBlueOrchidEmptiesPottedBlueOrchidAndGivesBlueOrchid(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.BLUE_ORCHID);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_allium")
    public void usingHandOnPottedAlliumEmptiesPottedAlliumAndGivesAllium(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.ALLIUM);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_azure_bluet")
    public void usingHandOnPottedAzureBluetEmptiesPottedAzureBluetAndGivesAzureBluet(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.AZURE_BLUET);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_red_tulip")
    public void usingHandOnPottedRedTulipEmptiesPottedRedTulipAndGivesRedTulip(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.RED_TULIP);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_orange_tulip")
    public void usingHandOnPottedOrangeTulipEmptiesPottedOrangeTulipAndGivesOrangeTulip(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.ORANGE_TULIP);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_white_tulip")
    public void usingHandOnPottedWhiteTulipEmptiesPottedWhiteTulipAndGivesWhiteTulip(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.WHITE_TULIP);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_pink_tulip")
    public void usingHandOnPottedPinkTulipEmptiesPottedPinkTulipAndGivesPinkTulip(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.PINK_TULIP);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_oxeye_daisy")
    public void usingHandOnPottedOxeyeDaisyEmptiesPottedOxeyeDaisyAndGivesOxeyeDaisy(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.OXEYE_DAISY);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_cornflower")
    public void usingHandOnPottedCornflowerEmptiesPottedCornflowerAndGivesCornflower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.CORNFLOWER);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_lily_of_the_valley")
    public void usingHandOnPottedLilyOfTheValleyEmptiesPottedLilyOfTheValleyAndGivesLilyOfTheValley(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.LILY_OF_THE_VALLEY);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_wither_rose")
    public void usingHandOnPottedWitherRoseEmptiesPottedWitherRoseAndGivesWitherRose(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.WITHER_ROSE);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_torchflower")
    public void usingHandOnPottedTorchflowerEmptiesPottedTorchflowerAndGivesTorchflower(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.TORCHFLOWER);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_red_mushroom")
    public void usingHandOnPottedRedMushroomEmptiesPottedRedMushroomAndGivesRedMushroom(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.RED_MUSHROOM);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_brown_mushroom")
    public void usingHandOnPottedBrownMushroomEmptiesPottedBrownMushroomAndGivesBrownMushroom(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        level.addFreshEntity(player);
        TestUtil.useBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItemIds.BROWN_MUSHROOM);
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }
}
