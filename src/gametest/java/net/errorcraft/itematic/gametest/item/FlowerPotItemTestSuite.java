package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItematicBlockItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.references.BlockItemIds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

public class FlowerPotItemTestSuite {
    private static final BlockPos FLOWER_POT_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOakSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.OAK.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_OAK_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOakSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.OAK.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.OAK.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingSpruceSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.SPRUCE.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_SPRUCE_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingSpruceSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.SPRUCE.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.SPRUCE.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBirchSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.BIRCH.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BIRCH_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBirchSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.BIRCH.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.BIRCH.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingJungleSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.JUNGLE.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_JUNGLE_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingJungleSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.JUNGLE.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.JUNGLE.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAcaciaSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.ACACIA.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_ACACIA_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAcaciaSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.ACACIA.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.ACACIA.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCherrySaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.CHERRY.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CHERRY_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCherrySaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.CHERRY.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.CHERRY.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDarkOakSaplingOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.DARK_OAK.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DARK_OAK_SAPLING);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDarkOakSaplingOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.DARK_OAK.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.DARK_OAK.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCrimsonFungusOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.CRIMSON_FUNGUS.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CRIMSON_FUNGUS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCrimsonFungusOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.CRIMSON_FUNGUS.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.CRIMSON_FUNGUS.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWarpedFungusOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.WARPED_FUNGUS.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WARPED_FUNGUS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWarpedFungusOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.WARPED_FUNGUS.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.WARPED_FUNGUS.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCrimsonRootsOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.CRIMSON_ROOTS.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CRIMSON_ROOTS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCrimsonRootsOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.CRIMSON_ROOTS.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.CRIMSON_ROOTS.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWarpedRootsOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.WARPED_ROOTS.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WARPED_ROOTS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWarpedRootsOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.WARPED_ROOTS.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.WARPED_ROOTS.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAzaleaBushOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.AZALEA.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_AZALEA);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAzaleaBushOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.AZALEA.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.AZALEA.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingFloweringAzaleaBushOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.FLOWERING_AZALEA.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_FLOWERING_AZALEA);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingFloweringAzaleaBushOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.FLOWERING_AZALEA.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.FLOWERING_AZALEA.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingMangrovePropaguleOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.MANGROVE.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_MANGROVE_PROPAGULE);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingMangrovePropaguleOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItematicBlockItemIds.MANGROVE.sapling().item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.MANGROVE.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCactusOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.CACTUS.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CACTUS);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCactusOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.CACTUS.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.CACTUS.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDeadBushOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.DEAD_BUSH.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DEAD_BUSH);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDeadBushOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.DEAD_BUSH.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.DEAD_BUSH.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBambooOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.BAMBOO.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BAMBOO);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBambooOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.BAMBOO.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.BAMBOO.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingFernOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.FERN.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_FERN);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingFernOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.FERN.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.FERN.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingDandelionOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.DANDELION.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DANDELION);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingDandelionOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.DANDELION.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.DANDELION.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingPoppyOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.POPPY.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dandelion")
    public void usingPoppyOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.POPPY.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.POPPY.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_DANDELION);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBlueOrchidOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.BLUE_ORCHID.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BLUE_ORCHID);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBlueOrchidOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.BLUE_ORCHID.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.BLUE_ORCHID.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAlliumOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.ALLIUM.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_ALLIUM);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAlliumOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.ALLIUM.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.ALLIUM.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingAzureBluetOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.AZURE_BLUET.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_AZURE_BLUET);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingAzureBluetOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.AZURE_BLUET.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.AZURE_BLUET.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingRedTulipOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.RED_TULIP.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_RED_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingRedTulipOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.RED_TULIP.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.RED_TULIP.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOrangeTulipOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.ORANGE_TULIP.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_ORANGE_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOrangeTulipOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.ORANGE_TULIP.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.ORANGE_TULIP.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWhiteTulipOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.WHITE_TULIP.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WHITE_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWhiteTulipOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.WHITE_TULIP.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.WHITE_TULIP.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingPinkTulipOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.PINK_TULIP.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_PINK_TULIP);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingPinkTulipOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.PINK_TULIP.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.PINK_TULIP.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingOxeyeDaisyOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.OXEYE_DAISY.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_OXEYE_DAISY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingOxeyeDaisyOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.OXEYE_DAISY.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.OXEYE_DAISY.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingCornflowerOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.CORNFLOWER.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_CORNFLOWER);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingCornflowerOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.CORNFLOWER.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.CORNFLOWER.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingLilyOfTheValleyOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.LILY_OF_THE_VALLEY.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_LILY_OF_THE_VALLEY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingLilyOfTheValleyOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.LILY_OF_THE_VALLEY.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.LILY_OF_THE_VALLEY.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingWitherRoseOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.WITHER_ROSE.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_WITHER_ROSE);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingWitherRoseOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.WITHER_ROSE.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.WITHER_ROSE.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingTorchflowerOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.TORCHFLOWER.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_TORCHFLOWER);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingTorchflowerOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.TORCHFLOWER.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.TORCHFLOWER.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingRedMushroomOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.RED_MUSHROOM.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_RED_MUSHROOM);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingRedMushroomOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.RED_MUSHROOM.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.RED_MUSHROOM.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.flower_pot")
    public void usingBrownMushroomOnFlowerPotReplacesFlowerPot(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.BROWN_MUSHROOM.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .isEmpty();
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_BROWN_MUSHROOM);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingBrownMushroomOnPottedFlowerPotDoesNotReplacePottedFlower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(BlockItemIds.BROWN_MUSHROOM.item())
        );
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.BROWN_MUSHROOM.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.POTTED_POPPY);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_oak_sapling")
    public void usingHandOnPottedOakSaplingEmptiesPottedOakSaplingAndGivesOakSapling(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.OAK.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_spruce_sapling")
    public void usingHandOnPottedSpruceSaplingEmptiesPottedSpruceSaplingAndGivesSpruceSapling(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.SPRUCE.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_birch_sapling")
    public void usingHandOnPottedBirchSaplingEmptiesPottedBirchSaplingAndGivesBirchSapling(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.BIRCH.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_jungle_sapling")
    public void usingHandOnPottedJungleSaplingEmptiesPottedJungleSaplingAndGivesJungleSapling(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.JUNGLE.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_acacia_sapling")
    public void usingHandOnPottedAcaciaSaplingEmptiesPottedAcaciaSaplingAndGivesAcaciaSapling(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.ACACIA.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_cherry_sapling")
    public void usingHandOnPottedCherrySaplingEmptiesPottedCherrySaplingAndGivesCherrySapling(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.CHERRY.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dark_oak_sapling")
    public void usingHandOnPottedDarkOakSaplingEmptiesPottedDarkOakSaplingAndGivesDarkOakSapling(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.DARK_OAK.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_crimson_fungus")
    public void usingHandOnPottedCrimsonFungusEmptiesPottedCrimsonFungusAndGivesCrimsonFungus(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.CRIMSON_FUNGUS.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_warped_fungus")
    public void usingHandOnPottedWarpedFungusEmptiesPottedWarpedFungusAndGivesWarpedFungus(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.WARPED_FUNGUS.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_crimson_roots")
    public void usingHandOnPottedCrimsonRootsEmptiesPottedCrimsonRootsAndGivesCrimsonRoots(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.CRIMSON_ROOTS.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_warped_roots")
    public void usingHandOnPottedWarpedRootsEmptiesPottedWarpedRootsAndGivesWarpedRoots(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.WARPED_ROOTS.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_azalea_bush")
    public void usingHandOnPottedAzaleaEmptiesPottedAzaleaAndGivesAzalea(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.AZALEA.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_flowering_azalea_bush")
    public void usingHandOnPottedFloweringAzaleaEmptiesPottedFloweringAzaleaAndGivesFloweringAzalea(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.FLOWERING_AZALEA.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_mangrove_propagule")
    public void usingHandOnPottedMangrovePropaguleEmptiesPottedMangrovePropaguleAndGivesMangrovePropagule(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(ItematicBlockItemIds.MANGROVE.sapling().item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_cactus")
    public void usingHandOnPottedCactusEmptiesPottedCactusAndGivesCactus(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.CACTUS.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dead_bush")
    public void usingHandOnPottedDeadBushEmptiesPottedDeadBushAndGivesDeadBush(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.DEAD_BUSH.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_bamboo")
    public void usingHandOnPottedBambooEmptiesPottedBambooAndGivesBamboo(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.BAMBOO.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_fern")
    public void usingHandOnPottedFernEmptiesPottedFernAndGivesFern(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.FERN.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_dandelion")
    public void usingHandOnPottedDandelionEmptiesPottedDandelionAndGivesDandelion(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.DANDELION.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_poppy")
    public void usingHandOnPottedPoppyEmptiesPottedPoppyAndGivesPoppy(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.POPPY.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_blue_orchid")
    public void usingHandOnPottedBlueOrchidEmptiesPottedBlueOrchidAndGivesBlueOrchid(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.BLUE_ORCHID.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_allium")
    public void usingHandOnPottedAlliumEmptiesPottedAlliumAndGivesAllium(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.ALLIUM.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_azure_bluet")
    public void usingHandOnPottedAzureBluetEmptiesPottedAzureBluetAndGivesAzureBluet(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.AZURE_BLUET.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_red_tulip")
    public void usingHandOnPottedRedTulipEmptiesPottedRedTulipAndGivesRedTulip(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.RED_TULIP.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_orange_tulip")
    public void usingHandOnPottedOrangeTulipEmptiesPottedOrangeTulipAndGivesOrangeTulip(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.ORANGE_TULIP.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_white_tulip")
    public void usingHandOnPottedWhiteTulipEmptiesPottedWhiteTulipAndGivesWhiteTulip(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.WHITE_TULIP.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_pink_tulip")
    public void usingHandOnPottedPinkTulipEmptiesPottedPinkTulipAndGivesPinkTulip(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.PINK_TULIP.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_oxeye_daisy")
    public void usingHandOnPottedOxeyeDaisyEmptiesPottedOxeyeDaisyAndGivesOxeyeDaisy(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.OXEYE_DAISY.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_cornflower")
    public void usingHandOnPottedCornflowerEmptiesPottedCornflowerAndGivesCornflower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.CORNFLOWER.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_lily_of_the_valley")
    public void usingHandOnPottedLilyOfTheValleyEmptiesPottedLilyOfTheValleyAndGivesLilyOfTheValley(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.LILY_OF_THE_VALLEY.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_wither_rose")
    public void usingHandOnPottedWitherRoseEmptiesPottedWitherRoseAndGivesWitherRose(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.WITHER_ROSE.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_torchflower")
    public void usingHandOnPottedTorchflowerEmptiesPottedTorchflowerAndGivesTorchflower(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.TORCHFLOWER.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_red_mushroom")
    public void usingHandOnPottedRedMushroomEmptiesPottedRedMushroomAndGivesRedMushroom(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.RED_MUSHROOM.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }

    @GameTest(structure = "itematic:item.flower_pot.platform.potted_brown_mushroom")
    public void usingHandOnPottedBrownMushroomEmptiesPottedBrownMushroomAndGivesBrownMushroom(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.interactWithBlock(helper, FLOWER_POT_POSITION, player, Direction.UP);
        helper.succeedIf(() -> {
            Assert.itemStack(helper, player.getMainHandItem())
                .is(BlockItemIds.BROWN_MUSHROOM.item());
            Assert.blockState(helper, FLOWER_POT_POSITION)
                .is(Blocks.FLOWER_POT);
        });
    }
}
