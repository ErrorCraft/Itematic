package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class PickBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.piston_head")
    public void getPickStackOnPistonHeadGivesPistonItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PISTON)
        );
    }

    @GameTest(structure = "itematic:block.piston_head.sticky")
    public void getPickStackOnStickyPistonHeadGivesStickyPistonItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.STICKY_PISTON)
        );
    }

    @GameTest(structure = "itematic:block.redstone_wire")
    public void getPickStackOnRedstoneWireGivesRedstoneItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.REDSTONE)
        );
    }

    @GameTest(structure = "itematic:block.tripwire")
    public void getPickStackOnTripwireGivesStringItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.STRING)
        );
    }

    @GameTest(structure = "itematic:block.wall_torch")
    public void getPickStackOnWallTorchGivesTorchItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.TORCH)
        );
    }

    @GameTest(structure = "itematic:block.redstone_wall_torch")
    public void getPickStackOnRedstoneWallTorchGivesRedstoneTorchItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.REDSTONE_TORCH)
        );
    }

    @GameTest(structure = "itematic:block.soul_wall_torch")
    public void getPickStackOnSoulWallTorchGivesSoulTorchItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SOUL_TORCH)
        );
    }

    @GameTest(structure = "itematic:block.oak_wall_sign")
    public void getPickStackOnOakWallSignGivesOakSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.OAK_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.spruce_wall_sign")
    public void getPickStackOnSpruceWallSignGivesSpruceSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SPRUCE_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.birch_wall_sign")
    public void getPickStackOnBirchWallSignGivesBirchSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BIRCH_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.acacia_wall_sign")
    public void getPickStackOnAcaciaWallSignGivesAcaciaSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ACACIA_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.cherry_wall_sign")
    public void getPickStackOnCherryWallSignGivesCherrySignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CHERRY_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.pale_oak_wall_sign")
    public void getPickStackOnPaleOakWallSignGivesPaleOakSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PALE_OAK_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.jungle_wall_sign")
    public void getPickStackOnJungleWallSignGivesJungleSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.JUNGLE_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.dark_oak_wall_sign")
    public void getPickStackOnDarkOakWallSignGivesDarkOakSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DARK_OAK_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.mangrove_wall_sign")
    public void getPickStackOnMangroveWallSignGivesMangroveSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.MANGROVE_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.bamboo_wall_sign")
    public void getPickStackOnBambooWallSignGivesBambooSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BAMBOO_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.crimson_wall_sign")
    public void getPickStackOnCrimsonWallSignGivesCrimsonSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CRIMSON_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.warped_wall_sign")
    public void getPickStackOnWarpedWallSignGivesWarpedSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WARPED_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.oak_wall_hanging_sign")
    public void getPickStackOnOakWallHangingSignGivesOakHangingSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.OAK_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.spruce_wall_hanging_sign")
    public void getPickStackOnSpruceWallHangingSignGivesSpruceHangingSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SPRUCE_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.birch_wall_hanging_sign")
    public void getPickStackOnBirchWallHangingSignGivesBirchHangingSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BIRCH_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.acacia_wall_hanging_sign")
    public void getPickStackOnAcaciaWallHangingSignGivesAcaciaHangingSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ACACIA_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.cherry_wall_hanging_sign")
    public void getPickStackOnCherryWallHangingSignGivesCherryHangingSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CHERRY_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.pale_oak_wall_hanging_sign")
    public void getPickStackOnPaleOakWallHangingSignGivesPaleOakHangingSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PALE_OAK_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.jungle_wall_hanging_sign")
    public void getPickStackOnJungleWallHangingSignGivesJungleHangingSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.JUNGLE_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.dark_oak_wall_hanging_sign")
    public void getPickStackOnDarkOakWallHangingSignGivesDarkOakHangingSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DARK_OAK_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.mangrove_wall_hanging_sign")
    public void getPickStackOnMangroveWallHangingSignGivesMangroveHangingSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.MANGROVE_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.crimson_wall_hanging_sign")
    public void getPickStackOnCrimsonWallHangingSignGivesCrimsonHangingSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CRIMSON_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.warped_wall_hanging_sign")
    public void getPickStackOnWarpedWallHangingSignGivesWarpedHangingSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WARPED_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.bamboo_wall_hanging_sign")
    public void getPickStackOnBambooWallHangingSignGivesBambooHangingSignItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BAMBOO_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.attached_pumpkin_stem")
    public void getPickStackOnAttachedPumpkinStemGivesPumpkinSeedsItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PUMPKIN_SEEDS)
        );
    }

    @GameTest(structure = "itematic:block.attached_melon_stem")
    public void getPickStackOnAttachedMelonStemGivesMelonSeedsItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.MELON_SEEDS)
        );
    }

    @GameTest(structure = "itematic:block.pumpkin_stem")
    public void getPickStackOnPumpkinStemGivesPumpkinSeedsItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PUMPKIN_SEEDS)
        );
    }

    @GameTest(structure = "itematic:block.melon_stem")
    public void getPickStackOnMelonStemGivesMelonSeedsItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.MELON_SEEDS)
        );
    }

    @GameTest(structure = "itematic:block.cocoa")
    public void getPickStackOnCocoaGivesCocoaBeansItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.COCOA_BEANS)
        );
    }

    @GameTest(structure = "itematic:block.carrots")
    public void getPickStackOnCarrotsGivesCarrotItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CARROT)
        );
    }

    @GameTest(structure = "itematic:block.potatoes")
    public void getPickStackOnPotatoesGivesPotatoItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.POTATO)
        );
    }

    @GameTest(structure = "itematic:block.torchflower_crop")
    public void getPickStackOnTorchflowerCropGivesTorchflowerSeedsItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.TORCHFLOWER_SEEDS)
        );
    }

    @GameTest(structure = "itematic:block.pitcher_crop")
    public void getPickStackOnPitcherCropGivesPitcherPodItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PITCHER_POD)
        );
    }

    @GameTest(structure = "itematic:block.beetroots")
    public void getPickStackOnBeetrootsGivesBeetrootSeedsItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BEETROOT_SEEDS)
        );
    }

    @GameTest(structure = "itematic:block.cave_vines")
    public void getPickStackOnCaveVinesGivesGlowBerriesItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.GLOW_BERRIES)
        );
    }

    @GameTest(structure = "itematic:block.cave_vines_plant")
    public void getPickStackOnCaveVinesPlantGivesGlowBerriesItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.GLOW_BERRIES)
        );
    }

    @GameTest(structure = "itematic:block.big_dripleaf_stem")
    public void getPickStackOnBigDripleafStemGivesBigDripleafItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BIG_DRIPLEAF)
        );
    }

    @GameTest(structure = "itematic:block.tall_seagrass")
    public void getPickStackOnTallSeagrassGivesSeagrassItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SEAGRASS)
        );
    }

    @GameTest(structure = "itematic:block.kelp_plant")
    public void getPickStackOnKelpPlantGivesKelpItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.KELP)
        );
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void getPickStackOnWaterCauldronGivesCauldronItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAULDRON)
        );
    }

    @GameTest(structure = "itematic:block.lava_cauldron")
    public void getPickStackOnLavaCauldronGivesCauldronItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAULDRON)
        );
    }

    @GameTest(structure = "itematic:block.powder_snow_cauldron")
    public void getPickStackOnPowderSnowCauldronGivesCauldronItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAULDRON)
        );
    }

    @GameTest(structure = "itematic:block.powder_snow")
    public void getPickStackOnPowderSnowGivesPowderSnowBucketItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.POWDER_SNOW_BUCKET)
        );
    }

    @GameTest(structure = "itematic:block.potted_torchflower")
    public void getPickStackOnPottedTorchflowerGivesTorchflowerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.TORCHFLOWER)
        );
    }

    @GameTest(structure = "itematic:block.potted_oak_sapling")
    public void getPickStackOnPottedOakSaplingGivesOakSaplingItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.OAK_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_spruce_sapling")
    public void getPickStackOnPottedSpruceSaplingGivesSpruceSaplingItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SPRUCE_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_birch_sapling")
    public void getPickStackOnPottedBirchSaplingGivesBirchSaplingItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BIRCH_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_jungle_sapling")
    public void getPickStackOnPottedJungleSaplingGivesJungleSaplingItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.JUNGLE_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_acacia_sapling")
    public void getPickStackOnPottedAcaciaSaplingGivesAcaciaSaplingItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ACACIA_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_cherry_sapling")
    public void getPickStackOnPottedCherrySaplingGivesCherrySaplingItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CHERRY_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_dark_oak_sapling")
    public void getPickStackOnPottedDarkOakSaplingGivesDarkOakSaplingItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DARK_OAK_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_mangrove_propagule")
    public void getPickStackOnPottedMangrovePropaguleGivesMangrovePropaguleItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.MANGROVE_PROPAGULE)
        );
    }

    @GameTest(structure = "itematic:block.potted_fern")
    public void getPickStackOnPottedFernGivesFernItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.FERN)
        );
    }

    @GameTest(structure = "itematic:block.potted_dandelion")
    public void getPickStackOnPottedDandelionGivesDandelionItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DANDELION)
        );
    }

    @GameTest(structure = "itematic:block.potted_poppy")
    public void getPickStackOnPottedPoppyGivesPoppyItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.POPPY)
        );
    }

    @GameTest(structure = "itematic:block.potted_blue_orchid")
    public void getPickStackOnPottedBlueOrchidGivesBlueOrchidItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BLUE_ORCHID)
        );
    }

    @GameTest(structure = "itematic:block.potted_allium")
    public void getPickStackOnPottedAlliumGivesAlliumItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ALLIUM)
        );
    }

    @GameTest(structure = "itematic:block.potted_azure_bluet")
    public void getPickStackOnPottedAzureBluetGivesAzureBluetItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.AZURE_BLUET)
        );
    }

    @GameTest(structure = "itematic:block.potted_red_tulip")
    public void getPickStackOnPottedRedTulipGivesRedTulipItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.RED_TULIP)
        );
    }

    @GameTest(structure = "itematic:block.potted_orange_tulip")
    public void getPickStackOnPottedOrangeTulipGivesOrangeTulipItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ORANGE_TULIP)
        );
    }

    @GameTest(structure = "itematic:block.potted_white_tulip")
    public void getPickStackOnPottedWhiteTulipGivesWhiteTulipItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WHITE_TULIP)
        );
    }

    @GameTest(structure = "itematic:block.potted_pink_tulip")
    public void getPickStackOnPottedPinkTulipGivesPinkTulipItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PINK_TULIP)
        );
    }

    @GameTest(structure = "itematic:block.potted_oxeye_daisy")
    public void getPickStackOnPottedOxeyeDaisyGivesOxeyeDaisyItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.OXEYE_DAISY)
        );
    }

    @GameTest(structure = "itematic:block.potted_cornflower")
    public void getPickStackOnPottedCornflowerGivesCornflowerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CORNFLOWER)
        );
    }

    @GameTest(structure = "itematic:block.potted_lily_of_the_valley")
    public void getPickStackOnPottedLilyOfTheValleyGivesLilyOfTheValleyItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.LILY_OF_THE_VALLEY)
        );
    }

    @GameTest(structure = "itematic:block.potted_wither_rose")
    public void getPickStackOnPottedWitherRoseGivesWitherRoseItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WITHER_ROSE)
        );
    }

    @GameTest(structure = "itematic:block.potted_red_mushroom")
    public void getPickStackOnPottedRedMushroomGivesRedMushroomItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.RED_MUSHROOM)
        );
    }

    @GameTest(structure = "itematic:block.potted_brown_mushroom")
    public void getPickStackOnPottedBrownMushroomGivesBrownMushroomItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BROWN_MUSHROOM)
        );
    }

    @GameTest(structure = "itematic:block.potted_dead_bush")
    public void getPickStackOnPottedDeadBushGivesDeadBushItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DEAD_BUSH)
        );
    }

    @GameTest(structure = "itematic:block.potted_cactus")
    public void getPickStackOnPottedCactusGivesCactusItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CACTUS)
        );
    }

    @GameTest(structure = "itematic:block.potted_bamboo")
    public void getPickStackOnPottedBambooGivesBambooItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BAMBOO)
        );
    }

    @GameTest(structure = "itematic:block.potted_crimson_fungus")
    public void getPickStackOnPottedCrimsonFungusGivesCrimsonFungusItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CRIMSON_FUNGUS)
        );
    }

    @GameTest(structure = "itematic:block.potted_warped_fungus")
    public void getPickStackOnPottedWarpedFungusGivesWarpedFungusItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WARPED_FUNGUS)
        );
    }

    @GameTest(structure = "itematic:block.potted_crimson_roots")
    public void getPickStackOnPottedCrimsonRootsGivesCrimsonRootsItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CRIMSON_ROOTS)
        );
    }

    @GameTest(structure = "itematic:block.potted_warped_roots")
    public void getPickStackOnPottedWarpedRootsGivesWarpedRootsItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WARPED_ROOTS)
        );
    }

    @GameTest(structure = "itematic:block.potted_azalea_bush")
    public void getPickStackOnPottedAzaleaBushGivesAzaleaItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.AZALEA)
        );
    }

    @GameTest(structure = "itematic:block.potted_flowering_azalea_bush")
    public void getPickStackOnPottedFloweringAzaleaBushGivesFloweringAzaleaItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.FLOWERING_AZALEA)
        );
    }

    @GameTest(structure = "itematic:block.potted_open_eyeblossom")
    public void getPickStackOnPottedOpenEyeblossomGivesOpenEyeblossomItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.OPEN_EYEBLOSSOM)
        );
    }

    @GameTest(structure = "itematic:block.potted_closed_eyeblossom")
    public void getPickStackOnPottedClosedEyeblossomGivesClosedEyeblossomItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CLOSED_EYEBLOSSOM)
        );
    }

    @GameTest(structure = "itematic:block.skeleton_wall_skull")
    public void getPickStackOnSkeletonWallSkullGivesSkeletonSkullItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SKELETON_SKULL)
        );
    }

    @GameTest(structure = "itematic:block.wither_skeleton_wall_skull")
    public void getPickStackOnWitherSkeletonWallSkullGivesWitherSkeletonSkullItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WITHER_SKELETON_SKULL)
        );
    }

    @GameTest(structure = "itematic:block.zombie_wall_head")
    public void getPickStackOnZombieWallHeadGivesZombieHeadItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ZOMBIE_HEAD)
        );
    }

    @GameTest(structure = "itematic:block.player_wall_head")
    public void getPickStackOnPlayerWallHeadGivesPlayerHeadItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PLAYER_HEAD)
        );
    }

    @GameTest(structure = "itematic:block.creeper_wall_head")
    public void getPickStackOnCreeperWallHeadGivesCreeperHeadItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CREEPER_HEAD)
        );
    }

    @GameTest(structure = "itematic:block.dragon_wall_head")
    public void getPickStackOnDragonWallHeadGivesDragonHeadItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DRAGON_HEAD)
        );
    }

    @GameTest(structure = "itematic:block.piglin_wall_head")
    public void getPickStackOnPiglinWallHeadGivesPiglinHeadItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PIGLIN_HEAD)
        );
    }

    @GameTest(structure = "itematic:block.white_wall_banner")
    public void getPickStackOnWhiteWallBannerGivesWhiteBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WHITE_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.orange_wall_banner")
    public void getPickStackOnOrangeWallBannerGivesOrangeBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ORANGE_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.magenta_wall_banner")
    public void getPickStackOnMagentaWallBannerGivesMagentaBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.MAGENTA_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.light_blue_wall_banner")
    public void getPickStackOnLightBlueWallBannerGivesLightBlueBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.LIGHT_BLUE_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.yellow_wall_banner")
    public void getPickStackOnYellowWallBannerGivesYellowBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.YELLOW_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.lime_wall_banner")
    public void getPickStackOnLimeWallBannerGivesLimeBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.LIME_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.pink_wall_banner")
    public void getPickStackOnPinkWallBannerGivesPinkBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PINK_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.gray_wall_banner")
    public void getPickStackOnGrayWallBannerGivesGrayBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.GRAY_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.light_gray_wall_banner")
    public void getPickStackOnLightGrayWallBannerGivesLightGrayBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.LIGHT_GRAY_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.cyan_wall_banner")
    public void getPickStackOnCyanWallBannerGivesCyanBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CYAN_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.purple_wall_banner")
    public void getPickStackOnPurpleWallBannerGivesPurpleBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PURPLE_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.blue_wall_banner")
    public void getPickStackOnBlueWallBannerGivesBlueBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BLUE_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.brown_wall_banner")
    public void getPickStackOnBrownWallBannerGivesBrownBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BROWN_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.green_wall_banner")
    public void getPickStackOnGreenWallBannerGivesGreenBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.GREEN_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.red_wall_banner")
    public void getPickStackOnRedWallBannerGivesRedBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.RED_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.black_wall_banner")
    public void getPickStackOnBlackWallBannerGivesBlackBannerItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BLACK_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.dead_tube_coral_wall_fan")
    public void getPickStackOnDeadTubeCoralWallFanGivesDeadTubeCoralFanItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DEAD_TUBE_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.dead_brain_coral_wall_fan")
    public void getPickStackOnDeadBrainCoralWallFanGivesDeadBrainCoralFanItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DEAD_BRAIN_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.dead_bubble_coral_wall_fan")
    public void getPickStackOnDeadBubbleCoralWallFanGivesDeadBubbleCoralFanItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DEAD_BUBBLE_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.dead_fire_coral_wall_fan")
    public void getPickStackOnDeadFireCoralWallFanGivesDeadFireCoralFanItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DEAD_FIRE_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.dead_horn_coral_wall_fan")
    public void getPickStackOnDeadHornCoralWallFanGivesDeadHornCoralFanItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DEAD_HORN_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.tube_coral_wall_fan")
    public void getPickStackOnTubeCoralWallFanGivesTubeCoralFanItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.TUBE_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.brain_coral_wall_fan")
    public void getPickStackOnBrainCoralWallFanGivesBrainCoralFanItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BRAIN_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.bubble_coral_wall_fan")
    public void getPickStackOnBubbleCoralWallFanGivesBubbleCoralFanItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BUBBLE_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.fire_coral_wall_fan")
    public void getPickStackOnFireCoralWallFanGivesFireCoralFanItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.FIRE_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.horn_coral_wall_fan")
    public void getPickStackOnHornCoralWallFanGivesHornCoralFanItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.HORN_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.bamboo_sapling")
    public void getPickStackOnBambooSaplingGivesBambooItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BAMBOO)
        );
    }

    @GameTest(structure = "itematic:block.sweet_berry_bush")
    public void getPickStackOnSweetBerryBushGivesSweetBerriesItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SWEET_BERRIES)
        );
    }

    @GameTest(structure = "itematic:block.weeping_vines_plant")
    public void getPickStackOnWeepingVinesPlantGivesWeepingVinesItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WEEPING_VINES)
        );
    }

    @GameTest(structure = "itematic:block.twisting_vines_plant")
    public void getPickStackOnTwistingVinesPlantGivesTwistingVinesItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.TWISTING_VINES)
        );
    }

    @GameTest(structure = "itematic:block.candle_cake")
    public void getPickStackOnCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.white_candle_cake")
    public void getPickStackOnWhiteCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.orange_candle_cake")
    public void getPickStackOnOrangeCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.magenta_candle_cake")
    public void getPickStackOnMagentaCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.light_blue_candle_cake")
    public void getPickStackOnLightBlueCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.yellow_candle_cake")
    public void getPickStackOnYellowCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.lime_candle_cake")
    public void getPickStackOnLimeCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.pink_candle_cake")
    public void getPickStackOnPinkCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.gray_candle_cake")
    public void getPickStackOnGrayCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.light_gray_candle_cake")
    public void getPickStackOnLightGrayCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.cyan_candle_cake")
    public void getPickStackOnCyanCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.purple_candle_cake")
    public void getPickStackOnPurpleCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.blue_candle_cake")
    public void getPickStackOnBlueCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.brown_candle_cake")
    public void getPickStackOnBrownCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.green_candle_cake")
    public void getPickStackOnGreenCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.red_candle_cake")
    public void getPickStackOnRedCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.black_candle_cake")
    public void getPickStackOnBlackCandleCakeGivesCakeItemStack(GameTestHelper context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(context.getLevel(), absolutePos, false);
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }
}
