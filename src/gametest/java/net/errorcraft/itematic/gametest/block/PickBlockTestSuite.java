package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

public class PickBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.piston_head")
    public void getPickStackOnPistonHeadGivesPistonItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PISTON)
        );
    }

    @GameTest(structure = "itematic:block.piston_head.sticky")
    public void getPickStackOnStickyPistonHeadGivesStickyPistonItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.STICKY_PISTON)
        );
    }

    @GameTest(structure = "itematic:block.redstone_wire")
    public void getPickStackOnRedstoneWireGivesRedstoneItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.REDSTONE)
        );
    }

    @GameTest(structure = "itematic:block.tripwire")
    public void getPickStackOnTripwireGivesStringItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.STRING)
        );
    }

    @GameTest(structure = "itematic:block.wall_torch")
    public void getPickStackOnWallTorchGivesTorchItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.TORCH)
        );
    }

    @GameTest(structure = "itematic:block.redstone_wall_torch")
    public void getPickStackOnRedstoneWallTorchGivesRedstoneTorchItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.REDSTONE_TORCH)
        );
    }

    @GameTest(structure = "itematic:block.soul_wall_torch")
    public void getPickStackOnSoulWallTorchGivesSoulTorchItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SOUL_TORCH)
        );
    }

    @GameTest(structure = "itematic:block.oak_wall_sign")
    public void getPickStackOnOakWallSignGivesOakSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.OAK_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.spruce_wall_sign")
    public void getPickStackOnSpruceWallSignGivesSpruceSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SPRUCE_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.birch_wall_sign")
    public void getPickStackOnBirchWallSignGivesBirchSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BIRCH_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.acacia_wall_sign")
    public void getPickStackOnAcaciaWallSignGivesAcaciaSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ACACIA_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.cherry_wall_sign")
    public void getPickStackOnCherryWallSignGivesCherrySignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CHERRY_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.pale_oak_wall_sign")
    public void getPickStackOnPaleOakWallSignGivesPaleOakSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PALE_OAK_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.jungle_wall_sign")
    public void getPickStackOnJungleWallSignGivesJungleSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.JUNGLE_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.dark_oak_wall_sign")
    public void getPickStackOnDarkOakWallSignGivesDarkOakSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DARK_OAK_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.mangrove_wall_sign")
    public void getPickStackOnMangroveWallSignGivesMangroveSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.MANGROVE_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.bamboo_wall_sign")
    public void getPickStackOnBambooWallSignGivesBambooSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BAMBOO_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.crimson_wall_sign")
    public void getPickStackOnCrimsonWallSignGivesCrimsonSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CRIMSON_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.warped_wall_sign")
    public void getPickStackOnWarpedWallSignGivesWarpedSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WARPED_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.oak_wall_hanging_sign")
    public void getPickStackOnOakWallHangingSignGivesOakHangingSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.OAK_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.spruce_wall_hanging_sign")
    public void getPickStackOnSpruceWallHangingSignGivesSpruceHangingSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SPRUCE_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.birch_wall_hanging_sign")
    public void getPickStackOnBirchWallHangingSignGivesBirchHangingSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BIRCH_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.acacia_wall_hanging_sign")
    public void getPickStackOnAcaciaWallHangingSignGivesAcaciaHangingSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ACACIA_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.cherry_wall_hanging_sign")
    public void getPickStackOnCherryWallHangingSignGivesCherryHangingSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CHERRY_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.pale_oak_wall_hanging_sign")
    public void getPickStackOnPaleOakWallHangingSignGivesPaleOakHangingSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PALE_OAK_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.jungle_wall_hanging_sign")
    public void getPickStackOnJungleWallHangingSignGivesJungleHangingSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.JUNGLE_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.dark_oak_wall_hanging_sign")
    public void getPickStackOnDarkOakWallHangingSignGivesDarkOakHangingSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DARK_OAK_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.mangrove_wall_hanging_sign")
    public void getPickStackOnMangroveWallHangingSignGivesMangroveHangingSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.MANGROVE_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.crimson_wall_hanging_sign")
    public void getPickStackOnCrimsonWallHangingSignGivesCrimsonHangingSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CRIMSON_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.warped_wall_hanging_sign")
    public void getPickStackOnWarpedWallHangingSignGivesWarpedHangingSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WARPED_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.bamboo_wall_hanging_sign")
    public void getPickStackOnBambooWallHangingSignGivesBambooHangingSignItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BAMBOO_HANGING_SIGN)
        );
    }

    @GameTest(structure = "itematic:block.attached_pumpkin_stem")
    public void getPickStackOnAttachedPumpkinStemGivesPumpkinSeedsItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PUMPKIN_SEEDS)
        );
    }

    @GameTest(structure = "itematic:block.attached_melon_stem")
    public void getPickStackOnAttachedMelonStemGivesMelonSeedsItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.MELON_SEEDS)
        );
    }

    @GameTest(structure = "itematic:block.pumpkin_stem")
    public void getPickStackOnPumpkinStemGivesPumpkinSeedsItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PUMPKIN_SEEDS)
        );
    }

    @GameTest(structure = "itematic:block.melon_stem")
    public void getPickStackOnMelonStemGivesMelonSeedsItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.MELON_SEEDS)
        );
    }

    @GameTest(structure = "itematic:block.cocoa")
    public void getPickStackOnCocoaGivesCocoaBeansItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.COCOA_BEANS)
        );
    }

    @GameTest(structure = "itematic:block.carrots")
    public void getPickStackOnCarrotsGivesCarrotItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CARROT)
        );
    }

    @GameTest(structure = "itematic:block.potatoes")
    public void getPickStackOnPotatoesGivesPotatoItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.POTATO)
        );
    }

    @GameTest(structure = "itematic:block.torchflower_crop")
    public void getPickStackOnTorchflowerCropGivesTorchflowerSeedsItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.TORCHFLOWER_SEEDS)
        );
    }

    @GameTest(structure = "itematic:block.pitcher_crop")
    public void getPickStackOnPitcherCropGivesPitcherPodItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PITCHER_POD)
        );
    }

    @GameTest(structure = "itematic:block.beetroots")
    public void getPickStackOnBeetrootsGivesBeetrootSeedsItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BEETROOT_SEEDS)
        );
    }

    @GameTest(structure = "itematic:block.cave_vines")
    public void getPickStackOnCaveVinesGivesGlowBerriesItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.GLOW_BERRIES)
        );
    }

    @GameTest(structure = "itematic:block.cave_vines_plant")
    public void getPickStackOnCaveVinesPlantGivesGlowBerriesItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.GLOW_BERRIES)
        );
    }

    @GameTest(structure = "itematic:block.big_dripleaf_stem")
    public void getPickStackOnBigDripleafStemGivesBigDripleafItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BIG_DRIPLEAF)
        );
    }

    @GameTest(structure = "itematic:block.tall_seagrass")
    public void getPickStackOnTallSeagrassGivesSeagrassItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SEAGRASS)
        );
    }

    @GameTest(structure = "itematic:block.kelp_plant")
    public void getPickStackOnKelpPlantGivesKelpItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.KELP)
        );
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void getPickStackOnWaterCauldronGivesCauldronItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAULDRON)
        );
    }

    @GameTest(structure = "itematic:block.lava_cauldron")
    public void getPickStackOnLavaCauldronGivesCauldronItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAULDRON)
        );
    }

    @GameTest(structure = "itematic:block.powder_snow_cauldron")
    public void getPickStackOnPowderSnowCauldronGivesCauldronItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAULDRON)
        );
    }

    @GameTest(structure = "itematic:block.powder_snow")
    public void getPickStackOnPowderSnowGivesPowderSnowBucketItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.POWDER_SNOW_BUCKET)
        );
    }

    @GameTest(structure = "itematic:block.potted_torchflower")
    public void getPickStackOnPottedTorchflowerGivesTorchflowerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.TORCHFLOWER)
        );
    }

    @GameTest(structure = "itematic:block.potted_oak_sapling")
    public void getPickStackOnPottedOakSaplingGivesOakSaplingItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.OAK_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_spruce_sapling")
    public void getPickStackOnPottedSpruceSaplingGivesSpruceSaplingItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SPRUCE_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_birch_sapling")
    public void getPickStackOnPottedBirchSaplingGivesBirchSaplingItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BIRCH_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_jungle_sapling")
    public void getPickStackOnPottedJungleSaplingGivesJungleSaplingItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.JUNGLE_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_acacia_sapling")
    public void getPickStackOnPottedAcaciaSaplingGivesAcaciaSaplingItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ACACIA_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_cherry_sapling")
    public void getPickStackOnPottedCherrySaplingGivesCherrySaplingItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CHERRY_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_dark_oak_sapling")
    public void getPickStackOnPottedDarkOakSaplingGivesDarkOakSaplingItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DARK_OAK_SAPLING)
        );
    }

    @GameTest(structure = "itematic:block.potted_mangrove_propagule")
    public void getPickStackOnPottedMangrovePropaguleGivesMangrovePropaguleItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.MANGROVE_PROPAGULE)
        );
    }

    @GameTest(structure = "itematic:block.potted_fern")
    public void getPickStackOnPottedFernGivesFernItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.FERN)
        );
    }

    @GameTest(structure = "itematic:block.potted_dandelion")
    public void getPickStackOnPottedDandelionGivesDandelionItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DANDELION)
        );
    }

    @GameTest(structure = "itematic:block.potted_poppy")
    public void getPickStackOnPottedPoppyGivesPoppyItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.POPPY)
        );
    }

    @GameTest(structure = "itematic:block.potted_blue_orchid")
    public void getPickStackOnPottedBlueOrchidGivesBlueOrchidItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BLUE_ORCHID)
        );
    }

    @GameTest(structure = "itematic:block.potted_allium")
    public void getPickStackOnPottedAlliumGivesAlliumItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ALLIUM)
        );
    }

    @GameTest(structure = "itematic:block.potted_azure_bluet")
    public void getPickStackOnPottedAzureBluetGivesAzureBluetItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.AZURE_BLUET)
        );
    }

    @GameTest(structure = "itematic:block.potted_red_tulip")
    public void getPickStackOnPottedRedTulipGivesRedTulipItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.RED_TULIP)
        );
    }

    @GameTest(structure = "itematic:block.potted_orange_tulip")
    public void getPickStackOnPottedOrangeTulipGivesOrangeTulipItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ORANGE_TULIP)
        );
    }

    @GameTest(structure = "itematic:block.potted_white_tulip")
    public void getPickStackOnPottedWhiteTulipGivesWhiteTulipItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WHITE_TULIP)
        );
    }

    @GameTest(structure = "itematic:block.potted_pink_tulip")
    public void getPickStackOnPottedPinkTulipGivesPinkTulipItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PINK_TULIP)
        );
    }

    @GameTest(structure = "itematic:block.potted_oxeye_daisy")
    public void getPickStackOnPottedOxeyeDaisyGivesOxeyeDaisyItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.OXEYE_DAISY)
        );
    }

    @GameTest(structure = "itematic:block.potted_cornflower")
    public void getPickStackOnPottedCornflowerGivesCornflowerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CORNFLOWER)
        );
    }

    @GameTest(structure = "itematic:block.potted_lily_of_the_valley")
    public void getPickStackOnPottedLilyOfTheValleyGivesLilyOfTheValleyItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.LILY_OF_THE_VALLEY)
        );
    }

    @GameTest(structure = "itematic:block.potted_wither_rose")
    public void getPickStackOnPottedWitherRoseGivesWitherRoseItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WITHER_ROSE)
        );
    }

    @GameTest(structure = "itematic:block.potted_red_mushroom")
    public void getPickStackOnPottedRedMushroomGivesRedMushroomItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.RED_MUSHROOM)
        );
    }

    @GameTest(structure = "itematic:block.potted_brown_mushroom")
    public void getPickStackOnPottedBrownMushroomGivesBrownMushroomItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BROWN_MUSHROOM)
        );
    }

    @GameTest(structure = "itematic:block.potted_dead_bush")
    public void getPickStackOnPottedDeadBushGivesDeadBushItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DEAD_BUSH)
        );
    }

    @GameTest(structure = "itematic:block.potted_cactus")
    public void getPickStackOnPottedCactusGivesCactusItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CACTUS)
        );
    }

    @GameTest(structure = "itematic:block.potted_bamboo")
    public void getPickStackOnPottedBambooGivesBambooItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BAMBOO)
        );
    }

    @GameTest(structure = "itematic:block.potted_crimson_fungus")
    public void getPickStackOnPottedCrimsonFungusGivesCrimsonFungusItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CRIMSON_FUNGUS)
        );
    }

    @GameTest(structure = "itematic:block.potted_warped_fungus")
    public void getPickStackOnPottedWarpedFungusGivesWarpedFungusItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WARPED_FUNGUS)
        );
    }

    @GameTest(structure = "itematic:block.potted_crimson_roots")
    public void getPickStackOnPottedCrimsonRootsGivesCrimsonRootsItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CRIMSON_ROOTS)
        );
    }

    @GameTest(structure = "itematic:block.potted_warped_roots")
    public void getPickStackOnPottedWarpedRootsGivesWarpedRootsItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WARPED_ROOTS)
        );
    }

    @GameTest(structure = "itematic:block.potted_azalea_bush")
    public void getPickStackOnPottedAzaleaBushGivesAzaleaItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.AZALEA)
        );
    }

    @GameTest(structure = "itematic:block.potted_flowering_azalea_bush")
    public void getPickStackOnPottedFloweringAzaleaBushGivesFloweringAzaleaItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.FLOWERING_AZALEA)
        );
    }

    @GameTest(structure = "itematic:block.potted_open_eyeblossom")
    public void getPickStackOnPottedOpenEyeblossomGivesOpenEyeblossomItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.OPEN_EYEBLOSSOM)
        );
    }

    @GameTest(structure = "itematic:block.potted_closed_eyeblossom")
    public void getPickStackOnPottedClosedEyeblossomGivesClosedEyeblossomItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CLOSED_EYEBLOSSOM)
        );
    }

    @GameTest(structure = "itematic:block.skeleton_wall_skull")
    public void getPickStackOnSkeletonWallSkullGivesSkeletonSkullItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SKELETON_SKULL)
        );
    }

    @GameTest(structure = "itematic:block.wither_skeleton_wall_skull")
    public void getPickStackOnWitherSkeletonWallSkullGivesWitherSkeletonSkullItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WITHER_SKELETON_SKULL)
        );
    }

    @GameTest(structure = "itematic:block.zombie_wall_head")
    public void getPickStackOnZombieWallHeadGivesZombieHeadItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ZOMBIE_HEAD)
        );
    }

    @GameTest(structure = "itematic:block.player_wall_head")
    public void getPickStackOnPlayerWallHeadGivesPlayerHeadItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PLAYER_HEAD)
        );
    }

    @GameTest(structure = "itematic:block.creeper_wall_head")
    public void getPickStackOnCreeperWallHeadGivesCreeperHeadItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CREEPER_HEAD)
        );
    }

    @GameTest(structure = "itematic:block.dragon_wall_head")
    public void getPickStackOnDragonWallHeadGivesDragonHeadItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DRAGON_HEAD)
        );
    }

    @GameTest(structure = "itematic:block.piglin_wall_head")
    public void getPickStackOnPiglinWallHeadGivesPiglinHeadItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PIGLIN_HEAD)
        );
    }

    @GameTest(structure = "itematic:block.white_wall_banner")
    public void getPickStackOnWhiteWallBannerGivesWhiteBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WHITE_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.orange_wall_banner")
    public void getPickStackOnOrangeWallBannerGivesOrangeBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.ORANGE_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.magenta_wall_banner")
    public void getPickStackOnMagentaWallBannerGivesMagentaBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.MAGENTA_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.light_blue_wall_banner")
    public void getPickStackOnLightBlueWallBannerGivesLightBlueBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.LIGHT_BLUE_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.yellow_wall_banner")
    public void getPickStackOnYellowWallBannerGivesYellowBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.YELLOW_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.lime_wall_banner")
    public void getPickStackOnLimeWallBannerGivesLimeBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.LIME_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.pink_wall_banner")
    public void getPickStackOnPinkWallBannerGivesPinkBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PINK_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.gray_wall_banner")
    public void getPickStackOnGrayWallBannerGivesGrayBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.GRAY_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.light_gray_wall_banner")
    public void getPickStackOnLightGrayWallBannerGivesLightGrayBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.LIGHT_GRAY_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.cyan_wall_banner")
    public void getPickStackOnCyanWallBannerGivesCyanBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CYAN_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.purple_wall_banner")
    public void getPickStackOnPurpleWallBannerGivesPurpleBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.PURPLE_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.blue_wall_banner")
    public void getPickStackOnBlueWallBannerGivesBlueBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BLUE_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.brown_wall_banner")
    public void getPickStackOnBrownWallBannerGivesBrownBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BROWN_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.green_wall_banner")
    public void getPickStackOnGreenWallBannerGivesGreenBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.GREEN_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.red_wall_banner")
    public void getPickStackOnRedWallBannerGivesRedBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.RED_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.black_wall_banner")
    public void getPickStackOnBlackWallBannerGivesBlackBannerItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BLACK_BANNER)
        );
    }

    @GameTest(structure = "itematic:block.dead_tube_coral_wall_fan")
    public void getPickStackOnDeadTubeCoralWallFanGivesDeadTubeCoralFanItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DEAD_TUBE_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.dead_brain_coral_wall_fan")
    public void getPickStackOnDeadBrainCoralWallFanGivesDeadBrainCoralFanItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DEAD_BRAIN_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.dead_bubble_coral_wall_fan")
    public void getPickStackOnDeadBubbleCoralWallFanGivesDeadBubbleCoralFanItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DEAD_BUBBLE_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.dead_fire_coral_wall_fan")
    public void getPickStackOnDeadFireCoralWallFanGivesDeadFireCoralFanItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DEAD_FIRE_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.dead_horn_coral_wall_fan")
    public void getPickStackOnDeadHornCoralWallFanGivesDeadHornCoralFanItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.DEAD_HORN_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.tube_coral_wall_fan")
    public void getPickStackOnTubeCoralWallFanGivesTubeCoralFanItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.TUBE_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.brain_coral_wall_fan")
    public void getPickStackOnBrainCoralWallFanGivesBrainCoralFanItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BRAIN_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.bubble_coral_wall_fan")
    public void getPickStackOnBubbleCoralWallFanGivesBubbleCoralFanItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BUBBLE_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.fire_coral_wall_fan")
    public void getPickStackOnFireCoralWallFanGivesFireCoralFanItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.FIRE_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.horn_coral_wall_fan")
    public void getPickStackOnHornCoralWallFanGivesHornCoralFanItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.HORN_CORAL_FAN)
        );
    }

    @GameTest(structure = "itematic:block.bamboo_sapling")
    public void getPickStackOnBambooSaplingGivesBambooItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.BAMBOO)
        );
    }

    @GameTest(structure = "itematic:block.sweet_berry_bush")
    public void getPickStackOnSweetBerryBushGivesSweetBerriesItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.SWEET_BERRIES)
        );
    }

    @GameTest(structure = "itematic:block.weeping_vines_plant")
    public void getPickStackOnWeepingVinesPlantGivesWeepingVinesItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.WEEPING_VINES)
        );
    }

    @GameTest(structure = "itematic:block.twisting_vines_plant")
    public void getPickStackOnTwistingVinesPlantGivesTwistingVinesItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.TWISTING_VINES)
        );
    }

    @GameTest(structure = "itematic:block.candle_cake")
    public void getPickStackOnCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.white_candle_cake")
    public void getPickStackOnWhiteCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.orange_candle_cake")
    public void getPickStackOnOrangeCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.magenta_candle_cake")
    public void getPickStackOnMagentaCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.light_blue_candle_cake")
    public void getPickStackOnLightBlueCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.yellow_candle_cake")
    public void getPickStackOnYellowCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.lime_candle_cake")
    public void getPickStackOnLimeCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.pink_candle_cake")
    public void getPickStackOnPinkCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.gray_candle_cake")
    public void getPickStackOnGrayCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.light_gray_candle_cake")
    public void getPickStackOnLightGrayCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.cyan_candle_cake")
    public void getPickStackOnCyanCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.purple_candle_cake")
    public void getPickStackOnPurpleCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.blue_candle_cake")
    public void getPickStackOnBlueCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.brown_candle_cake")
    public void getPickStackOnBrownCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.green_candle_cake")
    public void getPickStackOnGreenCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.red_candle_cake")
    public void getPickStackOnRedCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }

    @GameTest(structure = "itematic:block.black_candle_cake")
    public void getPickStackOnBlackCandleCakeGivesCakeItemStack(TestContext context) {
        BlockState state = context.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = context.getAbsolutePos(BLOCK_POSITION);
        ItemStack stack = state.getPickStack(context.getWorld(), absolutePos, false);
        context.addFinalTask(() -> Assert.itemStack(context, stack)
            .is(ItemKeys.CAKE)
        );
    }
}
