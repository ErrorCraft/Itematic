package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class FlowerPotItemTestSuite {
    private static final BlockPos FLOWER_POT_POSITION = new BlockPos(1, 1, 1);

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOakSaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OAK_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_OAK_SAPLING, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOakSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OAK_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.OAK_SAPLING);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingSpruceSaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SPRUCE_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_SPRUCE_SAPLING, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingSpruceSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SPRUCE_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.SPRUCE_SAPLING);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBirchSaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BIRCH_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_BIRCH_SAPLING, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBirchSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BIRCH_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.BIRCH_SAPLING);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingJungleSaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.JUNGLE_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_JUNGLE_SAPLING, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingJungleSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.JUNGLE_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.JUNGLE_SAPLING);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAcaciaSaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.ACACIA_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_ACACIA_SAPLING, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAcaciaSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.ACACIA_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.ACACIA_SAPLING);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCherrySaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CHERRY_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_CHERRY_SAPLING, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCherrySaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CHERRY_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.CHERRY_SAPLING);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDarkOakSaplingOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.DARK_OAK_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_DARK_OAK_SAPLING, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDarkOakSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.DARK_OAK_SAPLING);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.DARK_OAK_SAPLING);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCrimsonFungusOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CRIMSON_FUNGUS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_CRIMSON_FUNGUS, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCrimsonFungusOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CRIMSON_FUNGUS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.CRIMSON_FUNGUS);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWarpedFungusOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WARPED_FUNGUS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_WARPED_FUNGUS, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWarpedFungusOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WARPED_FUNGUS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.WARPED_FUNGUS);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCrimsonRootsOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CRIMSON_ROOTS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_CRIMSON_ROOTS, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCrimsonRootsOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CRIMSON_ROOTS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.CRIMSON_ROOTS);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWarpedRootsOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WARPED_ROOTS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_WARPED_ROOTS, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWarpedRootsOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WARPED_ROOTS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.WARPED_ROOTS);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAzaleaBushOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.AZALEA);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_AZALEA_BUSH, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAzaleaBushOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.AZALEA);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.AZALEA);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingFloweringAzaleaBushOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.FLOWERING_AZALEA);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_FLOWERING_AZALEA_BUSH, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingFloweringAzaleaBushOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.FLOWERING_AZALEA);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.FLOWERING_AZALEA);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingMangrovePropaguleOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.MANGROVE_PROPAGULE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_MANGROVE_PROPAGULE, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingMangrovePropaguleOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.MANGROVE_PROPAGULE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.MANGROVE_PROPAGULE);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCactusOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CACTUS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_CACTUS, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCactusOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CACTUS);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.CACTUS);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDeadBushOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.DEAD_BUSH);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_DEAD_BUSH, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDeadBushOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.DEAD_BUSH);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.DEAD_BUSH);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBambooOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BAMBOO);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_BAMBOO, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBambooOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BAMBOO);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.BAMBOO);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingFernOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.FERN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_FERN, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingFernOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.FERN);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.FERN);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDandelionOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.DANDELION);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_DANDELION, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDandelionOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.DANDELION);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.DANDELION);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingPoppyOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.POPPY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_dandelion")
    public void usingPoppyOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.POPPY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.POPPY);
            context.expectBlock(Blocks.POTTED_DANDELION, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBlueOrchidOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BLUE_ORCHID);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_BLUE_ORCHID, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBlueOrchidOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BLUE_ORCHID);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.BLUE_ORCHID);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAlliumOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.ALLIUM);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_ALLIUM, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAlliumOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.ALLIUM);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.ALLIUM);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAzureBluetOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.AZURE_BLUET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_AZURE_BLUET, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAzureBluetOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.AZURE_BLUET);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.AZURE_BLUET);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingRedTulipOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.RED_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_RED_TULIP, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingRedTulipOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.RED_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.RED_TULIP);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOrangeTulipOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.ORANGE_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_ORANGE_TULIP, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOrangeTulipOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.ORANGE_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.ORANGE_TULIP);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWhiteTulipOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WHITE_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_WHITE_TULIP, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWhiteTulipOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WHITE_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.WHITE_TULIP);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingPinkTulipOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.PINK_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_PINK_TULIP, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingPinkTulipOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.PINK_TULIP);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.PINK_TULIP);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOxeyeDaisyOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OXEYE_DAISY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_OXEYE_DAISY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOxeyeDaisyOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.OXEYE_DAISY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.OXEYE_DAISY);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCornflowerOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CORNFLOWER);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_CORNFLOWER, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCornflowerOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CORNFLOWER);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.CORNFLOWER);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingLilyOfTheValleyOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.LILY_OF_THE_VALLEY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_LILY_OF_THE_VALLEY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingLilyOfTheValleyOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.LILY_OF_THE_VALLEY);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.LILY_OF_THE_VALLEY);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWitherRoseOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WITHER_ROSE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_WITHER_ROSE, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWitherRoseOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WITHER_ROSE);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.WITHER_ROSE);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingTorchflowerOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.TORCHFLOWER);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_TORCHFLOWER, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingTorchflowerOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.TORCHFLOWER);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.TORCHFLOWER);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingRedMushroomOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.RED_MUSHROOM);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_RED_MUSHROOM, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingRedMushroomOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.RED_MUSHROOM);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.RED_MUSHROOM);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBrownMushroomOnFlowerPotReplacesFlowerPot(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BROWN_MUSHROOM);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsEmpty(stack);
            context.expectBlock(Blocks.POTTED_BROWN_MUSHROOM, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBrownMushroomOnPottedFlowerPotDoesNotReplacePottedFlower(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BROWN_MUSHROOM);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        world.spawnEntity(player);
        TestUtil.useStackOnBlockInside(context, player, stack, FLOWER_POT_POSITION, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getStackInHand(Hand.MAIN_HAND), ItemKeys.BROWN_MUSHROOM);
            context.expectBlock(Blocks.POTTED_POPPY, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_oak_sapling")
    public void usingHandOnPottedOakSaplingEmptiesPottedOakSaplingAndGivesOakSapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.OAK_SAPLING);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_spruce_sapling")
    public void usingHandOnPottedSpruceSaplingEmptiesPottedSpruceSaplingAndGivesSpruceSapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.SPRUCE_SAPLING);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_birch_sapling")
    public void usingHandOnPottedBirchSaplingEmptiesPottedBirchSaplingAndGivesBirchSapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.BIRCH_SAPLING);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_jungle_sapling")
    public void usingHandOnPottedJungleSaplingEmptiesPottedJungleSaplingAndGivesJungleSapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.JUNGLE_SAPLING);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_acacia_sapling")
    public void usingHandOnPottedAcaciaSaplingEmptiesPottedAcaciaSaplingAndGivesAcaciaSapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.ACACIA_SAPLING);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_cherry_sapling")
    public void usingHandOnPottedCherrySaplingEmptiesPottedCherrySaplingAndGivesCherrySapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.CHERRY_SAPLING);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_dark_oak_sapling")
    public void usingHandOnPottedDarkOakSaplingEmptiesPottedDarkOakSaplingAndGivesDarkOakSapling(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.DARK_OAK_SAPLING);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_crimson_fungus")
    public void usingHandOnPottedCrimsonFungusEmptiesPottedCrimsonFungusAndGivesCrimsonFungus(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.CRIMSON_FUNGUS);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_warped_fungus")
    public void usingHandOnPottedWarpedFungusEmptiesPottedWarpedFungusAndGivesWarpedFungus(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.WARPED_FUNGUS);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_crimson_roots")
    public void usingHandOnPottedCrimsonRootsEmptiesPottedCrimsonRootsAndGivesCrimsonRoots(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.CRIMSON_ROOTS);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_warped_roots")
    public void usingHandOnPottedWarpedRootsEmptiesPottedWarpedRootsAndGivesWarpedRoots(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.WARPED_ROOTS);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_azalea_bush")
    public void usingHandOnPottedAzaleaEmptiesPottedAzaleaAndGivesAzalea(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.AZALEA);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_flowering_azalea_bush")
    public void usingHandOnPottedFloweringAzaleaEmptiesPottedFloweringAzaleaAndGivesFloweringAzalea(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.FLOWERING_AZALEA);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_mangrove_propagule")
    public void usingHandOnPottedMangrovePropaguleEmptiesPottedMangrovePropaguleAndGivesMangrovePropagule(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.MANGROVE_PROPAGULE);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_cactus")
    public void usingHandOnPottedCactusEmptiesPottedCactusAndGivesCactus(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.CACTUS);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_dead_bush")
    public void usingHandOnPottedDeadBushEmptiesPottedDeadBushAndGivesDeadBush(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.DEAD_BUSH);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_bamboo")
    public void usingHandOnPottedBambooEmptiesPottedBambooAndGivesBamboo(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.BAMBOO);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_fern")
    public void usingHandOnPottedFernEmptiesPottedFernAndGivesFern(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.FERN);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_dandelion")
    public void usingHandOnPottedDandelionEmptiesPottedDandelionAndGivesDandelion(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.DANDELION);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingHandOnPottedPoppyEmptiesPottedPoppyAndGivesPoppy(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.POPPY);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_blue_orchid")
    public void usingHandOnPottedBlueOrchidEmptiesPottedBlueOrchidAndGivesBlueOrchid(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.BLUE_ORCHID);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_allium")
    public void usingHandOnPottedAlliumEmptiesPottedAlliumAndGivesAllium(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.ALLIUM);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_azure_bluet")
    public void usingHandOnPottedAzureBluetEmptiesPottedAzureBluetAndGivesAzureBluet(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.AZURE_BLUET);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_red_tulip")
    public void usingHandOnPottedRedTulipEmptiesPottedRedTulipAndGivesRedTulip(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.RED_TULIP);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_orange_tulip")
    public void usingHandOnPottedOrangeTulipEmptiesPottedOrangeTulipAndGivesOrangeTulip(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.ORANGE_TULIP);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_white_tulip")
    public void usingHandOnPottedWhiteTulipEmptiesPottedWhiteTulipAndGivesWhiteTulip(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.WHITE_TULIP);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_pink_tulip")
    public void usingHandOnPottedPinkTulipEmptiesPottedPinkTulipAndGivesPinkTulip(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.PINK_TULIP);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_oxeye_daisy")
    public void usingHandOnPottedOxeyeDaisyEmptiesPottedOxeyeDaisyAndGivesOxeyeDaisy(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.OXEYE_DAISY);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_cornflower")
    public void usingHandOnPottedCornflowerEmptiesPottedCornflowerAndGivesCornflower(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.CORNFLOWER);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_lily_of_the_valley")
    public void usingHandOnPottedLilyOfTheValleyEmptiesPottedLilyOfTheValleyAndGivesLilyOfTheValley(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.LILY_OF_THE_VALLEY);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_wither_rose")
    public void usingHandOnPottedWitherRoseEmptiesPottedWitherRoseAndGivesWitherRose(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.WITHER_ROSE);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_torchflower")
    public void usingHandOnPottedTorchflowerEmptiesPottedTorchflowerAndGivesTorchflower(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.TORCHFLOWER);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_red_mushroom")
    public void usingHandOnPottedRedMushroomEmptiesPottedRedMushroomAndGivesRedMushroom(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.RED_MUSHROOM);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }

    @GameTest(templateName = "itematic:item.flower_pot.platform.potted_brown_mushroom")
    public void usingHandOnPottedBrownMushroomEmptiesPottedBrownMushroomAndGivesBrownMushroom(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        world.spawnEntity(player);
        TestUtil.useBlock(context, FLOWER_POT_POSITION, player, Direction.UP);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(player.getMainHandStack(), ItemKeys.BROWN_MUSHROOM);
            context.expectBlock(Blocks.FLOWER_POT, FLOWER_POT_POSITION);
        });
    }
}
