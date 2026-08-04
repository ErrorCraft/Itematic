package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
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
    public void usingOakSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack oakSapling = world.itematic$createStack(ItemKeys.OAK_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oakSapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakSapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, oakSapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_OAK_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOakSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack oakSapling = world.itematic$createStack(ItemKeys.OAK_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oakSapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakSapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.OAK_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingSpruceSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack spruceSapling = world.itematic$createStack(ItemKeys.SPRUCE_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, spruceSapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, spruceSapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, spruceSapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_SPRUCE_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingSpruceSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack spruceSapling = world.itematic$createStack(ItemKeys.SPRUCE_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, spruceSapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, spruceSapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.SPRUCE_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBirchSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack birchSapling = world.itematic$createStack(ItemKeys.BIRCH_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, birchSapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, birchSapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, birchSapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BIRCH_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBirchSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack birchSapling = world.itematic$createStack(ItemKeys.BIRCH_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, birchSapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, birchSapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.BIRCH_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingJungleSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack jungleSapling = world.itematic$createStack(ItemKeys.JUNGLE_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, jungleSapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, jungleSapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, jungleSapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_JUNGLE_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingJungleSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack jungleSapling = world.itematic$createStack(ItemKeys.JUNGLE_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, jungleSapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, jungleSapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.JUNGLE_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAcaciaSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack acaciaSapling = world.itematic$createStack(ItemKeys.ACACIA_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, acaciaSapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, acaciaSapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, acaciaSapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_ACACIA_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAcaciaSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack acaciaSapling = world.itematic$createStack(ItemKeys.ACACIA_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, acaciaSapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, acaciaSapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.ACACIA_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCherrySaplingOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack cherrySapling = world.itematic$createStack(ItemKeys.CHERRY_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, cherrySapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, cherrySapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, cherrySapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CHERRY_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCherrySaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack cherrySapling = world.itematic$createStack(ItemKeys.CHERRY_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, cherrySapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, cherrySapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.CHERRY_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDarkOakSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack darkOakSapling = world.itematic$createStack(ItemKeys.DARK_OAK_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, darkOakSapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, darkOakSapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, darkOakSapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DARK_OAK_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDarkOakSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack darkOakSapling = world.itematic$createStack(ItemKeys.DARK_OAK_SAPLING);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, darkOakSapling);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, darkOakSapling, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.DARK_OAK_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCrimsonFungusOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack crimsonFungus = world.itematic$createStack(ItemKeys.CRIMSON_FUNGUS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, crimsonFungus);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, crimsonFungus, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, crimsonFungus)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CRIMSON_FUNGUS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCrimsonFungusOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack crimsonFungus = world.itematic$createStack(ItemKeys.CRIMSON_FUNGUS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, crimsonFungus);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, crimsonFungus, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.CRIMSON_FUNGUS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWarpedFungusOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack warpedFungus = world.itematic$createStack(ItemKeys.WARPED_FUNGUS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, warpedFungus);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, warpedFungus, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, warpedFungus)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WARPED_FUNGUS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWarpedFungusOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack warpedFungus = world.itematic$createStack(ItemKeys.WARPED_FUNGUS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, warpedFungus);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, warpedFungus, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.WARPED_FUNGUS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCrimsonRootsOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack crimsonRoots = world.itematic$createStack(ItemKeys.CRIMSON_ROOTS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, crimsonRoots);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, crimsonRoots, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, crimsonRoots)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CRIMSON_ROOTS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCrimsonRootsOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack crimsonRoots = world.itematic$createStack(ItemKeys.CRIMSON_ROOTS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, crimsonRoots);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, crimsonRoots, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.CRIMSON_ROOTS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWarpedRootsOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack warpedRoots = world.itematic$createStack(ItemKeys.WARPED_ROOTS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, warpedRoots);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, warpedRoots, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, warpedRoots)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WARPED_ROOTS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWarpedRootsOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack warpedRoots = world.itematic$createStack(ItemKeys.WARPED_ROOTS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, warpedRoots);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, warpedRoots, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.WARPED_ROOTS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAzaleaBushOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack azalea = world.itematic$createStack(ItemKeys.AZALEA);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, azalea);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, azalea, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, azalea)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_AZALEA);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAzaleaBushOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack azalea = world.itematic$createStack(ItemKeys.AZALEA);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, azalea);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, azalea, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.AZALEA);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingFloweringAzaleaBushOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack floweringAzalea = world.itematic$createStack(ItemKeys.FLOWERING_AZALEA);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, floweringAzalea);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, floweringAzalea, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, floweringAzalea)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_FLOWERING_AZALEA);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingFloweringAzaleaBushOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack floweringAzalea = world.itematic$createStack(ItemKeys.FLOWERING_AZALEA);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, floweringAzalea);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, floweringAzalea, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.FLOWERING_AZALEA);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingMangrovePropaguleOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack mangrovePropagule = world.itematic$createStack(ItemKeys.MANGROVE_PROPAGULE);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, mangrovePropagule);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, mangrovePropagule, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, mangrovePropagule)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_MANGROVE_PROPAGULE);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingMangrovePropaguleOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack mangrovePropagule = world.itematic$createStack(ItemKeys.MANGROVE_PROPAGULE);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, mangrovePropagule);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, mangrovePropagule, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.MANGROVE_PROPAGULE);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCactusOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack cactus = world.itematic$createStack(ItemKeys.CACTUS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, cactus);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, cactus, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, cactus)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CACTUS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCactusOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack cactus = world.itematic$createStack(ItemKeys.CACTUS);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, cactus);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, cactus, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.CACTUS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDeadBushOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack deadBush = world.itematic$createStack(ItemKeys.DEAD_BUSH);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, deadBush);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, deadBush, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, deadBush)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DEAD_BUSH);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDeadBushOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack deadBush = world.itematic$createStack(ItemKeys.DEAD_BUSH);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, deadBush);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, deadBush, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.DEAD_BUSH);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBambooOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack bamboo = world.itematic$createStack(ItemKeys.BAMBOO);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, bamboo);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, bamboo, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, bamboo)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BAMBOO);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBambooOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack bamboo = world.itematic$createStack(ItemKeys.BAMBOO);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, bamboo);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, bamboo, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.BAMBOO);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingFernOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack fern = world.itematic$createStack(ItemKeys.FERN);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, fern);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, fern, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, fern)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_FERN);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingFernOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack fern = world.itematic$createStack(ItemKeys.FERN);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, fern);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, fern, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.FERN);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDandelionOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack dandelion = world.itematic$createStack(ItemKeys.DANDELION);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, dandelion);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, dandelion, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, dandelion)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DANDELION);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDandelionOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack dandelion = world.itematic$createStack(ItemKeys.DANDELION);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, dandelion);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, dandelion, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.DANDELION);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingPoppyOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack poppy = world.itematic$createStack(ItemKeys.POPPY);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, poppy);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, poppy, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, poppy)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dandelion")
    public void usingPoppyOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack poppy = world.itematic$createStack(ItemKeys.POPPY);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, poppy);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, poppy, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.POPPY);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DANDELION);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBlueOrchidOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack blueOrchid = world.itematic$createStack(ItemKeys.BLUE_ORCHID);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, blueOrchid);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, blueOrchid, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, blueOrchid)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BLUE_ORCHID);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBlueOrchidOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack blueOrchid = world.itematic$createStack(ItemKeys.BLUE_ORCHID);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, blueOrchid);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, blueOrchid, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.BLUE_ORCHID);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAlliumOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack allium = world.itematic$createStack(ItemKeys.ALLIUM);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, allium);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, allium, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, allium)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_ALLIUM);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAlliumOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack allium = world.itematic$createStack(ItemKeys.ALLIUM);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, allium);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, allium, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.ALLIUM);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAzureBluetOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack azureBluet = world.itematic$createStack(ItemKeys.AZURE_BLUET);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, azureBluet);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, azureBluet, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, azureBluet)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_AZURE_BLUET);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAzureBluetOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack azureBluet = world.itematic$createStack(ItemKeys.AZURE_BLUET);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, azureBluet);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, azureBluet, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.AZURE_BLUET);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingRedTulipOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack redTulip = world.itematic$createStack(ItemKeys.RED_TULIP);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, redTulip);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, redTulip, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, redTulip)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_RED_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingRedTulipOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack redTulip = world.itematic$createStack(ItemKeys.RED_TULIP);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, redTulip);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, redTulip, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.RED_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOrangeTulipOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack orangeTulip = world.itematic$createStack(ItemKeys.ORANGE_TULIP);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, orangeTulip);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, orangeTulip, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, orangeTulip)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_ORANGE_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOrangeTulipOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack orangeTulip = world.itematic$createStack(ItemKeys.ORANGE_TULIP);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, orangeTulip);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, orangeTulip, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.ORANGE_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWhiteTulipOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack whiteTulip = world.itematic$createStack(ItemKeys.WHITE_TULIP);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, whiteTulip);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, whiteTulip, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, whiteTulip)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WHITE_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWhiteTulipOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack whiteTulip = world.itematic$createStack(ItemKeys.WHITE_TULIP);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, whiteTulip);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, whiteTulip, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.WHITE_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingPinkTulipOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack pinkTulip = world.itematic$createStack(ItemKeys.PINK_TULIP);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, pinkTulip);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, pinkTulip, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, pinkTulip)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_PINK_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingPinkTulipOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack pinkTulip = world.itematic$createStack(ItemKeys.PINK_TULIP);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, pinkTulip);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, pinkTulip, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.PINK_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOxeyeDaisyOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack oxeyeDaisy = world.itematic$createStack(ItemKeys.OXEYE_DAISY);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oxeyeDaisy);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oxeyeDaisy, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, oxeyeDaisy)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_OXEYE_DAISY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOxeyeDaisyOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack oxeyeDaisy = world.itematic$createStack(ItemKeys.OXEYE_DAISY);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, oxeyeDaisy);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oxeyeDaisy, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.OXEYE_DAISY);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCornflowerOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack cornflower = world.itematic$createStack(ItemKeys.CORNFLOWER);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, cornflower);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, cornflower, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, cornflower)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CORNFLOWER);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCornflowerOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack cornflower = world.itematic$createStack(ItemKeys.CORNFLOWER);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, cornflower);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, cornflower, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.CORNFLOWER);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingLilyOfTheValleyOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack lilyOfTheValley = world.itematic$createStack(ItemKeys.LILY_OF_THE_VALLEY);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, lilyOfTheValley);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, lilyOfTheValley, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, lilyOfTheValley)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_LILY_OF_THE_VALLEY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingLilyOfTheValleyOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack lilyOfTheValley = world.itematic$createStack(ItemKeys.LILY_OF_THE_VALLEY);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, lilyOfTheValley);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, lilyOfTheValley, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.LILY_OF_THE_VALLEY);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWitherRoseOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack witherRose = world.itematic$createStack(ItemKeys.WITHER_ROSE);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, witherRose);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, witherRose, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, witherRose)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WITHER_ROSE);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWitherRoseOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack witherRose = world.itematic$createStack(ItemKeys.WITHER_ROSE);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, witherRose);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, witherRose, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.WITHER_ROSE);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingTorchflowerOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack torchflower = world.itematic$createStack(ItemKeys.TORCHFLOWER);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, torchflower);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, torchflower, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, torchflower)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_TORCHFLOWER);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingTorchflowerOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack torchflower = world.itematic$createStack(ItemKeys.TORCHFLOWER);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, torchflower);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, torchflower, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.TORCHFLOWER);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingRedMushroomOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack redMushroom = world.itematic$createStack(ItemKeys.RED_MUSHROOM);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, redMushroom);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, redMushroom, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, redMushroom)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_RED_MUSHROOM);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingRedMushroomOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack redMushroom = world.itematic$createStack(ItemKeys.RED_MUSHROOM);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, redMushroom);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, redMushroom, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.RED_MUSHROOM);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBrownMushroomOnFlowerPotReplacesFlowerPot(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack brownMushroom = world.itematic$createStack(ItemKeys.BROWN_MUSHROOM);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, brownMushroom);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, brownMushroom, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, brownMushroom)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BROWN_MUSHROOM);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBrownMushroomOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack brownMushroom = world.itematic$createStack(ItemKeys.BROWN_MUSHROOM);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, brownMushroom);
        world.addFreshEntity(player);
        TestUtil.useStackOnBlockInside(context, player, brownMushroom, FLOWER_POT_POSITION, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.BROWN_MUSHROOM);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_oak_sapling")
    public void usingHandOnPottedOakSaplingEmptiesPottedOakSaplingAndGivesOakSapling(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.OAK_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_spruce_sapling")
    public void usingHandOnPottedSpruceSaplingEmptiesPottedSpruceSaplingAndGivesSpruceSapling(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.SPRUCE_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_birch_sapling")
    public void usingHandOnPottedBirchSaplingEmptiesPottedBirchSaplingAndGivesBirchSapling(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.BIRCH_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_jungle_sapling")
    public void usingHandOnPottedJungleSaplingEmptiesPottedJungleSaplingAndGivesJungleSapling(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.JUNGLE_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_acacia_sapling")
    public void usingHandOnPottedAcaciaSaplingEmptiesPottedAcaciaSaplingAndGivesAcaciaSapling(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.ACACIA_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_cherry_sapling")
    public void usingHandOnPottedCherrySaplingEmptiesPottedCherrySaplingAndGivesCherrySapling(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.CHERRY_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dark_oak_sapling")
    public void usingHandOnPottedDarkOakSaplingEmptiesPottedDarkOakSaplingAndGivesDarkOakSapling(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.DARK_OAK_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_crimson_fungus")
    public void usingHandOnPottedCrimsonFungusEmptiesPottedCrimsonFungusAndGivesCrimsonFungus(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.CRIMSON_FUNGUS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_warped_fungus")
    public void usingHandOnPottedWarpedFungusEmptiesPottedWarpedFungusAndGivesWarpedFungus(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.WARPED_FUNGUS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_crimson_roots")
    public void usingHandOnPottedCrimsonRootsEmptiesPottedCrimsonRootsAndGivesCrimsonRoots(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.CRIMSON_ROOTS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_warped_roots")
    public void usingHandOnPottedWarpedRootsEmptiesPottedWarpedRootsAndGivesWarpedRoots(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.WARPED_ROOTS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_azalea_bush")
    public void usingHandOnPottedAzaleaEmptiesPottedAzaleaAndGivesAzalea(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.AZALEA);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_flowering_azalea_bush")
    public void usingHandOnPottedFloweringAzaleaEmptiesPottedFloweringAzaleaAndGivesFloweringAzalea(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.FLOWERING_AZALEA);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_mangrove_propagule")
    public void usingHandOnPottedMangrovePropaguleEmptiesPottedMangrovePropaguleAndGivesMangrovePropagule(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.MANGROVE_PROPAGULE);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_cactus")
    public void usingHandOnPottedCactusEmptiesPottedCactusAndGivesCactus(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.CACTUS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dead_bush")
    public void usingHandOnPottedDeadBushEmptiesPottedDeadBushAndGivesDeadBush(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.DEAD_BUSH);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_bamboo")
    public void usingHandOnPottedBambooEmptiesPottedBambooAndGivesBamboo(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.BAMBOO);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_fern")
    public void usingHandOnPottedFernEmptiesPottedFernAndGivesFern(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.FERN);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dandelion")
    public void usingHandOnPottedDandelionEmptiesPottedDandelionAndGivesDandelion(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.DANDELION);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingHandOnPottedPoppyEmptiesPottedPoppyAndGivesPoppy(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.POPPY);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_blue_orchid")
    public void usingHandOnPottedBlueOrchidEmptiesPottedBlueOrchidAndGivesBlueOrchid(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.BLUE_ORCHID);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_allium")
    public void usingHandOnPottedAlliumEmptiesPottedAlliumAndGivesAllium(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.ALLIUM);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_azure_bluet")
    public void usingHandOnPottedAzureBluetEmptiesPottedAzureBluetAndGivesAzureBluet(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.AZURE_BLUET);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_red_tulip")
    public void usingHandOnPottedRedTulipEmptiesPottedRedTulipAndGivesRedTulip(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.RED_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_orange_tulip")
    public void usingHandOnPottedOrangeTulipEmptiesPottedOrangeTulipAndGivesOrangeTulip(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.ORANGE_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_white_tulip")
    public void usingHandOnPottedWhiteTulipEmptiesPottedWhiteTulipAndGivesWhiteTulip(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.WHITE_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_pink_tulip")
    public void usingHandOnPottedPinkTulipEmptiesPottedPinkTulipAndGivesPinkTulip(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.PINK_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_oxeye_daisy")
    public void usingHandOnPottedOxeyeDaisyEmptiesPottedOxeyeDaisyAndGivesOxeyeDaisy(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.OXEYE_DAISY);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_cornflower")
    public void usingHandOnPottedCornflowerEmptiesPottedCornflowerAndGivesCornflower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.CORNFLOWER);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_lily_of_the_valley")
    public void usingHandOnPottedLilyOfTheValleyEmptiesPottedLilyOfTheValleyAndGivesLilyOfTheValley(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.LILY_OF_THE_VALLEY);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_wither_rose")
    public void usingHandOnPottedWitherRoseEmptiesPottedWitherRoseAndGivesWitherRose(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.WITHER_ROSE);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_torchflower")
    public void usingHandOnPottedTorchflowerEmptiesPottedTorchflowerAndGivesTorchflower(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.TORCHFLOWER);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_red_mushroom")
    public void usingHandOnPottedRedMushroomEmptiesPottedRedMushroomAndGivesRedMushroom(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.RED_MUSHROOM);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_brown_mushroom")
    public void usingHandOnPottedBrownMushroomEmptiesPottedBrownMushroomAndGivesBrownMushroom(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.succeedIf(() -> {
            Assert.itemStack(context, player.getMainHandItem())
                .is(ItemKeys.BROWN_MUSHROOM);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }
}
