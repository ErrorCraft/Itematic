package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class FlowerPotItemTestSuite {
    private static final BlockPos FLOWER_POT_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOakSaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack oakSapling = world.itematic$createStack(ItemKeys.OAK_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, oakSapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakSapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, oakSapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_OAK_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOakSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack oakSapling = world.itematic$createStack(ItemKeys.OAK_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, oakSapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oakSapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.OAK_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingSpruceSaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack spruceSapling = world.itematic$createStack(ItemKeys.SPRUCE_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, spruceSapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, spruceSapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, spruceSapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_SPRUCE_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingSpruceSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack spruceSapling = world.itematic$createStack(ItemKeys.SPRUCE_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, spruceSapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, spruceSapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.SPRUCE_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBirchSaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack birchSapling = world.itematic$createStack(ItemKeys.BIRCH_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, birchSapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, birchSapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, birchSapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BIRCH_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBirchSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack birchSapling = world.itematic$createStack(ItemKeys.BIRCH_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, birchSapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, birchSapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.BIRCH_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingJungleSaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack jungleSapling = world.itematic$createStack(ItemKeys.JUNGLE_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, jungleSapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, jungleSapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, jungleSapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_JUNGLE_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingJungleSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack jungleSapling = world.itematic$createStack(ItemKeys.JUNGLE_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, jungleSapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, jungleSapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.JUNGLE_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAcaciaSaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack acaciaSapling = world.itematic$createStack(ItemKeys.ACACIA_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, acaciaSapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, acaciaSapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, acaciaSapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_ACACIA_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAcaciaSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack acaciaSapling = world.itematic$createStack(ItemKeys.ACACIA_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, acaciaSapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, acaciaSapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.ACACIA_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCherrySaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack cherrySapling = world.itematic$createStack(ItemKeys.CHERRY_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, cherrySapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, cherrySapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, cherrySapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CHERRY_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCherrySaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack cherrySapling = world.itematic$createStack(ItemKeys.CHERRY_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, cherrySapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, cherrySapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.CHERRY_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDarkOakSaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack darkOakSapling = world.itematic$createStack(ItemKeys.DARK_OAK_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, darkOakSapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, darkOakSapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, darkOakSapling)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DARK_OAK_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDarkOakSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack darkOakSapling = world.itematic$createStack(ItemKeys.DARK_OAK_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, darkOakSapling);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, darkOakSapling, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.DARK_OAK_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCrimsonFungusOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack crimsonFungus = world.itematic$createStack(ItemKeys.CRIMSON_FUNGUS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, crimsonFungus);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, crimsonFungus, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, crimsonFungus)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CRIMSON_FUNGUS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCrimsonFungusOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack crimsonFungus = world.itematic$createStack(ItemKeys.CRIMSON_FUNGUS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, crimsonFungus);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, crimsonFungus, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.CRIMSON_FUNGUS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWarpedFungusOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack warpedFungus = world.itematic$createStack(ItemKeys.WARPED_FUNGUS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, warpedFungus);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, warpedFungus, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, warpedFungus)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WARPED_FUNGUS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWarpedFungusOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack warpedFungus = world.itematic$createStack(ItemKeys.WARPED_FUNGUS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, warpedFungus);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, warpedFungus, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.WARPED_FUNGUS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCrimsonRootsOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack crimsonRoots = world.itematic$createStack(ItemKeys.CRIMSON_ROOTS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, crimsonRoots);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, crimsonRoots, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, crimsonRoots)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CRIMSON_ROOTS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCrimsonRootsOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack crimsonRoots = world.itematic$createStack(ItemKeys.CRIMSON_ROOTS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, crimsonRoots);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, crimsonRoots, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.CRIMSON_ROOTS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWarpedRootsOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack warpedRoots = world.itematic$createStack(ItemKeys.WARPED_ROOTS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, warpedRoots);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, warpedRoots, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, warpedRoots)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WARPED_ROOTS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWarpedRootsOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack warpedRoots = world.itematic$createStack(ItemKeys.WARPED_ROOTS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, warpedRoots);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, warpedRoots, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.WARPED_ROOTS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAzaleaBushOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack azalea = world.itematic$createStack(ItemKeys.AZALEA);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, azalea);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, azalea, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, azalea)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_AZALEA_BUSH);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAzaleaBushOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack azalea = world.itematic$createStack(ItemKeys.AZALEA);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, azalea);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, azalea, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.AZALEA);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingFloweringAzaleaBushOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack floweringAzalea = world.itematic$createStack(ItemKeys.FLOWERING_AZALEA);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, floweringAzalea);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, floweringAzalea, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, floweringAzalea)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_FLOWERING_AZALEA_BUSH);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingFloweringAzaleaBushOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack floweringAzalea = world.itematic$createStack(ItemKeys.FLOWERING_AZALEA);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, floweringAzalea);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, floweringAzalea, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.FLOWERING_AZALEA);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingMangrovePropaguleOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack mangrovePropagule = world.itematic$createStack(ItemKeys.MANGROVE_PROPAGULE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, mangrovePropagule);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, mangrovePropagule, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, mangrovePropagule)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_MANGROVE_PROPAGULE);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingMangrovePropaguleOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack mangrovePropagule = world.itematic$createStack(ItemKeys.MANGROVE_PROPAGULE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, mangrovePropagule);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, mangrovePropagule, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.MANGROVE_PROPAGULE);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCactusOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack cactus = world.itematic$createStack(ItemKeys.CACTUS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, cactus);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, cactus, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, cactus)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CACTUS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCactusOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack cactus = world.itematic$createStack(ItemKeys.CACTUS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, cactus);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, cactus, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.CACTUS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDeadBushOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack deadBush = world.itematic$createStack(ItemKeys.DEAD_BUSH);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, deadBush);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, deadBush, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, deadBush)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DEAD_BUSH);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDeadBushOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack deadBush = world.itematic$createStack(ItemKeys.DEAD_BUSH);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, deadBush);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, deadBush, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.DEAD_BUSH);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBambooOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack bamboo = world.itematic$createStack(ItemKeys.BAMBOO);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, bamboo);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, bamboo, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, bamboo)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BAMBOO);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBambooOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack bamboo = world.itematic$createStack(ItemKeys.BAMBOO);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, bamboo);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, bamboo, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.BAMBOO);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingFernOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack fern = world.itematic$createStack(ItemKeys.FERN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, fern);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, fern, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, fern)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_FERN);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingFernOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack fern = world.itematic$createStack(ItemKeys.FERN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, fern);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, fern, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.FERN);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDandelionOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack dandelion = world.itematic$createStack(ItemKeys.DANDELION);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, dandelion);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, dandelion, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, dandelion)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DANDELION);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDandelionOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack dandelion = world.itematic$createStack(ItemKeys.DANDELION);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, dandelion);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, dandelion, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.DANDELION);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingPoppyOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack poppy = world.itematic$createStack(ItemKeys.POPPY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, poppy);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, poppy, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, poppy)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dandelion")
    public void usingPoppyOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack poppy = world.itematic$createStack(ItemKeys.POPPY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, poppy);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, poppy, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.POPPY);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DANDELION);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBlueOrchidOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack blueOrchid = world.itematic$createStack(ItemKeys.BLUE_ORCHID);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, blueOrchid);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, blueOrchid, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, blueOrchid)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BLUE_ORCHID);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBlueOrchidOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack blueOrchid = world.itematic$createStack(ItemKeys.BLUE_ORCHID);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, blueOrchid);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, blueOrchid, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.BLUE_ORCHID);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAlliumOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack allium = world.itematic$createStack(ItemKeys.ALLIUM);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, allium);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, allium, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, allium)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_ALLIUM);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAlliumOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack allium = world.itematic$createStack(ItemKeys.ALLIUM);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, allium);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, allium, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.ALLIUM);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAzureBluetOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack azureBluet = world.itematic$createStack(ItemKeys.AZURE_BLUET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, azureBluet);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, azureBluet, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, azureBluet)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_AZURE_BLUET);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAzureBluetOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack azureBluet = world.itematic$createStack(ItemKeys.AZURE_BLUET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, azureBluet);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, azureBluet, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.AZURE_BLUET);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingRedTulipOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack redTulip = world.itematic$createStack(ItemKeys.RED_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, redTulip);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, redTulip, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, redTulip)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_RED_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingRedTulipOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack redTulip = world.itematic$createStack(ItemKeys.RED_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, redTulip);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, redTulip, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.RED_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOrangeTulipOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack orangeTulip = world.itematic$createStack(ItemKeys.ORANGE_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, orangeTulip);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, orangeTulip, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, orangeTulip)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_ORANGE_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOrangeTulipOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack orangeTulip = world.itematic$createStack(ItemKeys.ORANGE_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, orangeTulip);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, orangeTulip, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.ORANGE_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWhiteTulipOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack whiteTulip = world.itematic$createStack(ItemKeys.WHITE_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, whiteTulip);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, whiteTulip, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, whiteTulip)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WHITE_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWhiteTulipOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack whiteTulip = world.itematic$createStack(ItemKeys.WHITE_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, whiteTulip);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, whiteTulip, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.WHITE_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingPinkTulipOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack pinkTulip = world.itematic$createStack(ItemKeys.PINK_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, pinkTulip);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, pinkTulip, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, pinkTulip)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_PINK_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingPinkTulipOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack pinkTulip = world.itematic$createStack(ItemKeys.PINK_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, pinkTulip);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, pinkTulip, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.PINK_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOxeyeDaisyOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack oxeyeDaisy = world.itematic$createStack(ItemKeys.OXEYE_DAISY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, oxeyeDaisy);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oxeyeDaisy, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, oxeyeDaisy)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_OXEYE_DAISY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOxeyeDaisyOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack oxeyeDaisy = world.itematic$createStack(ItemKeys.OXEYE_DAISY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, oxeyeDaisy);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, oxeyeDaisy, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.OXEYE_DAISY);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCornflowerOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack cornflower = world.itematic$createStack(ItemKeys.CORNFLOWER);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, cornflower);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, cornflower, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, cornflower)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CORNFLOWER);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCornflowerOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack cornflower = world.itematic$createStack(ItemKeys.CORNFLOWER);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, cornflower);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, cornflower, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.CORNFLOWER);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingLilyOfTheValleyOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack lilyOfTheValley = world.itematic$createStack(ItemKeys.LILY_OF_THE_VALLEY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, lilyOfTheValley);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, lilyOfTheValley, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, lilyOfTheValley)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_LILY_OF_THE_VALLEY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingLilyOfTheValleyOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack lilyOfTheValley = world.itematic$createStack(ItemKeys.LILY_OF_THE_VALLEY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, lilyOfTheValley);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, lilyOfTheValley, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.LILY_OF_THE_VALLEY);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWitherRoseOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack witherRose = world.itematic$createStack(ItemKeys.WITHER_ROSE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, witherRose);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, witherRose, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, witherRose)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WITHER_ROSE);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWitherRoseOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack witherRose = world.itematic$createStack(ItemKeys.WITHER_ROSE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, witherRose);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, witherRose, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.WITHER_ROSE);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingTorchflowerOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack torchflower = world.itematic$createStack(ItemKeys.TORCHFLOWER);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, torchflower);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, torchflower, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, torchflower)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_TORCHFLOWER);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingTorchflowerOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack torchflower = world.itematic$createStack(ItemKeys.TORCHFLOWER);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, torchflower);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, torchflower, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.TORCHFLOWER);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingRedMushroomOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack redMushroom = world.itematic$createStack(ItemKeys.RED_MUSHROOM);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, redMushroom);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, redMushroom, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, redMushroom)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_RED_MUSHROOM);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingRedMushroomOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack redMushroom = world.itematic$createStack(ItemKeys.RED_MUSHROOM);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, redMushroom);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, redMushroom, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.RED_MUSHROOM);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBrownMushroomOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack brownMushroom = world.itematic$createStack(ItemKeys.BROWN_MUSHROOM);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, brownMushroom);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, brownMushroom, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, brownMushroom)
                .isEmpty();
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BROWN_MUSHROOM);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBrownMushroomOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack brownMushroom = world.itematic$createStack(ItemKeys.BROWN_MUSHROOM);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, brownMushroom);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, brownMushroom, FLOWER_POT_POSITION, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.BROWN_MUSHROOM);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_oak_sapling")
    public void usingHandOnPottedOakSaplingEmptiesPottedOakSaplingAndGivesOakSapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.OAK_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_spruce_sapling")
    public void usingHandOnPottedSpruceSaplingEmptiesPottedSpruceSaplingAndGivesSpruceSapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.SPRUCE_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_birch_sapling")
    public void usingHandOnPottedBirchSaplingEmptiesPottedBirchSaplingAndGivesBirchSapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.BIRCH_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_jungle_sapling")
    public void usingHandOnPottedJungleSaplingEmptiesPottedJungleSaplingAndGivesJungleSapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.JUNGLE_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_acacia_sapling")
    public void usingHandOnPottedAcaciaSaplingEmptiesPottedAcaciaSaplingAndGivesAcaciaSapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.ACACIA_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_cherry_sapling")
    public void usingHandOnPottedCherrySaplingEmptiesPottedCherrySaplingAndGivesCherrySapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.CHERRY_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dark_oak_sapling")
    public void usingHandOnPottedDarkOakSaplingEmptiesPottedDarkOakSaplingAndGivesDarkOakSapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.DARK_OAK_SAPLING);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_crimson_fungus")
    public void usingHandOnPottedCrimsonFungusEmptiesPottedCrimsonFungusAndGivesCrimsonFungus(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.CRIMSON_FUNGUS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_warped_fungus")
    public void usingHandOnPottedWarpedFungusEmptiesPottedWarpedFungusAndGivesWarpedFungus(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.WARPED_FUNGUS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_crimson_roots")
    public void usingHandOnPottedCrimsonRootsEmptiesPottedCrimsonRootsAndGivesCrimsonRoots(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.CRIMSON_ROOTS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_warped_roots")
    public void usingHandOnPottedWarpedRootsEmptiesPottedWarpedRootsAndGivesWarpedRoots(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.WARPED_ROOTS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_azalea_bush")
    public void usingHandOnPottedAzaleaEmptiesPottedAzaleaAndGivesAzalea(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.AZALEA);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_flowering_azalea_bush")
    public void usingHandOnPottedFloweringAzaleaEmptiesPottedFloweringAzaleaAndGivesFloweringAzalea(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.FLOWERING_AZALEA);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_mangrove_propagule")
    public void usingHandOnPottedMangrovePropaguleEmptiesPottedMangrovePropaguleAndGivesMangrovePropagule(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.MANGROVE_PROPAGULE);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_cactus")
    public void usingHandOnPottedCactusEmptiesPottedCactusAndGivesCactus(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.CACTUS);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dead_bush")
    public void usingHandOnPottedDeadBushEmptiesPottedDeadBushAndGivesDeadBush(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.DEAD_BUSH);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_bamboo")
    public void usingHandOnPottedBambooEmptiesPottedBambooAndGivesBamboo(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.BAMBOO);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_fern")
    public void usingHandOnPottedFernEmptiesPottedFernAndGivesFern(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.FERN);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dandelion")
    public void usingHandOnPottedDandelionEmptiesPottedDandelionAndGivesDandelion(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.DANDELION);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingHandOnPottedPoppyEmptiesPottedPoppyAndGivesPoppy(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.POPPY);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_blue_orchid")
    public void usingHandOnPottedBlueOrchidEmptiesPottedBlueOrchidAndGivesBlueOrchid(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.BLUE_ORCHID);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_allium")
    public void usingHandOnPottedAlliumEmptiesPottedAlliumAndGivesAllium(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.ALLIUM);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_azure_bluet")
    public void usingHandOnPottedAzureBluetEmptiesPottedAzureBluetAndGivesAzureBluet(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.AZURE_BLUET);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_red_tulip")
    public void usingHandOnPottedRedTulipEmptiesPottedRedTulipAndGivesRedTulip(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.RED_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_orange_tulip")
    public void usingHandOnPottedOrangeTulipEmptiesPottedOrangeTulipAndGivesOrangeTulip(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.ORANGE_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_white_tulip")
    public void usingHandOnPottedWhiteTulipEmptiesPottedWhiteTulipAndGivesWhiteTulip(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.WHITE_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_pink_tulip")
    public void usingHandOnPottedPinkTulipEmptiesPottedPinkTulipAndGivesPinkTulip(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.PINK_TULIP);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_oxeye_daisy")
    public void usingHandOnPottedOxeyeDaisyEmptiesPottedOxeyeDaisyAndGivesOxeyeDaisy(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.OXEYE_DAISY);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_cornflower")
    public void usingHandOnPottedCornflowerEmptiesPottedCornflowerAndGivesCornflower(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.CORNFLOWER);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_lily_of_the_valley")
    public void usingHandOnPottedLilyOfTheValleyEmptiesPottedLilyOfTheValleyAndGivesLilyOfTheValley(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.LILY_OF_THE_VALLEY);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_wither_rose")
    public void usingHandOnPottedWitherRoseEmptiesPottedWitherRoseAndGivesWitherRose(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.WITHER_ROSE);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_torchflower")
    public void usingHandOnPottedTorchflowerEmptiesPottedTorchflowerAndGivesTorchflower(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.TORCHFLOWER);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_red_mushroom")
    public void usingHandOnPottedRedMushroomEmptiesPottedRedMushroomAndGivesRedMushroom(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.RED_MUSHROOM);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_brown_mushroom")
    public void usingHandOnPottedBrownMushroomEmptiesPottedBrownMushroomAndGivesBrownMushroom(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addFinalTask(() -> {
            Assert.itemStack(context, player.getMainHandStack())
                .is(ItemKeys.BROWN_MUSHROOM);
            Assert.blockState(context, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }
}
