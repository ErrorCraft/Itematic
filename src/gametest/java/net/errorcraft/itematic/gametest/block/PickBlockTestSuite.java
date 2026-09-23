package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItematicBlockItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.references.BlockItemIds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class PickBlockTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.piston_head")
    public void getPickStackOnPistonHeadGivesPistonItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.PISTON.item())
        );
    }

    @GameTest(structure = "itematic:block.piston_head.sticky")
    public void getPickStackOnStickyPistonHeadGivesStickyPistonItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.STICKY_PISTON.item())
        );
    }

    @GameTest(structure = "itematic:block.redstone_wire")
    public void getPickStackOnRedstoneWireGivesRedstoneItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.REDSTONE_DUST.item())
        );
    }

    @GameTest(structure = "itematic:block.tripwire")
    public void getPickStackOnTripwireGivesStringItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.TRIPWIRE.item())
        );
    }

    @GameTest(structure = "itematic:block.wall_torch")
    public void getPickStackOnWallTorchGivesTorchItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.TORCH.item())
        );
    }

    @GameTest(structure = "itematic:block.redstone_wall_torch")
    public void getPickStackOnRedstoneWallTorchGivesRedstoneTorchItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.REDSTONE_TORCH.item())
        );
    }

    @GameTest(structure = "itematic:block.soul_wall_torch")
    public void getPickStackOnSoulWallTorchGivesSoulTorchItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.SOUL_TORCH.item())
        );
    }

    @GameTest(structure = "itematic:block.oak_wall_sign")
    public void getPickStackOnOakWallSignGivesOakSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.OAK.sign().item())
        );
    }

    @GameTest(structure = "itematic:block.spruce_wall_sign")
    public void getPickStackOnSpruceWallSignGivesSpruceSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.SPRUCE.sign().item())
        );
    }

    @GameTest(structure = "itematic:block.birch_wall_sign")
    public void getPickStackOnBirchWallSignGivesBirchSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.BIRCH.sign().item())
        );
    }

    @GameTest(structure = "itematic:block.acacia_wall_sign")
    public void getPickStackOnAcaciaWallSignGivesAcaciaSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.ACACIA.sign().item())
        );
    }

    @GameTest(structure = "itematic:block.cherry_wall_sign")
    public void getPickStackOnCherryWallSignGivesCherrySignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.CHERRY.sign().item())
        );
    }

    @GameTest(structure = "itematic:block.pale_oak_wall_sign")
    public void getPickStackOnPaleOakWallSignGivesPaleOakSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.PALE_OAK.sign().item())
        );
    }

    @GameTest(structure = "itematic:block.jungle_wall_sign")
    public void getPickStackOnJungleWallSignGivesJungleSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.JUNGLE.sign().item())
        );
    }

    @GameTest(structure = "itematic:block.dark_oak_wall_sign")
    public void getPickStackOnDarkOakWallSignGivesDarkOakSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.DARK_OAK.sign().item())
        );
    }

    @GameTest(structure = "itematic:block.mangrove_wall_sign")
    public void getPickStackOnMangroveWallSignGivesMangroveSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.MANGROVE.sign().item())
        );
    }

    @GameTest(structure = "itematic:block.bamboo_wall_sign")
    public void getPickStackOnBambooWallSignGivesBambooSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.BAMBOO.sign().item())
        );
    }

    @GameTest(structure = "itematic:block.crimson_wall_sign")
    public void getPickStackOnCrimsonWallSignGivesCrimsonSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.CRIMSON.sign().item())
        );
    }

    @GameTest(structure = "itematic:block.warped_wall_sign")
    public void getPickStackOnWarpedWallSignGivesWarpedSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.WARPED.sign().item())
        );
    }

    @GameTest(structure = "itematic:block.oak_wall_hanging_sign")
    public void getPickStackOnOakWallHangingSignGivesOakHangingSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.OAK.hangingSign().item())
        );
    }

    @GameTest(structure = "itematic:block.spruce_wall_hanging_sign")
    public void getPickStackOnSpruceWallHangingSignGivesSpruceHangingSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.SPRUCE.hangingSign().item())
        );
    }

    @GameTest(structure = "itematic:block.birch_wall_hanging_sign")
    public void getPickStackOnBirchWallHangingSignGivesBirchHangingSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.BIRCH.hangingSign().item())
        );
    }

    @GameTest(structure = "itematic:block.acacia_wall_hanging_sign")
    public void getPickStackOnAcaciaWallHangingSignGivesAcaciaHangingSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.ACACIA.hangingSign().item())
        );
    }

    @GameTest(structure = "itematic:block.cherry_wall_hanging_sign")
    public void getPickStackOnCherryWallHangingSignGivesCherryHangingSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.CHERRY.hangingSign().item())
        );
    }

    @GameTest(structure = "itematic:block.pale_oak_wall_hanging_sign")
    public void getPickStackOnPaleOakWallHangingSignGivesPaleOakHangingSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.PALE_OAK.hangingSign().item())
        );
    }

    @GameTest(structure = "itematic:block.jungle_wall_hanging_sign")
    public void getPickStackOnJungleWallHangingSignGivesJungleHangingSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.JUNGLE.hangingSign().item())
        );
    }

    @GameTest(structure = "itematic:block.dark_oak_wall_hanging_sign")
    public void getPickStackOnDarkOakWallHangingSignGivesDarkOakHangingSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.DARK_OAK.hangingSign().item())
        );
    }

    @GameTest(structure = "itematic:block.mangrove_wall_hanging_sign")
    public void getPickStackOnMangroveWallHangingSignGivesMangroveHangingSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.MANGROVE.hangingSign().item())
        );
    }

    @GameTest(structure = "itematic:block.crimson_wall_hanging_sign")
    public void getPickStackOnCrimsonWallHangingSignGivesCrimsonHangingSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.CRIMSON.hangingSign().item())
        );
    }

    @GameTest(structure = "itematic:block.warped_wall_hanging_sign")
    public void getPickStackOnWarpedWallHangingSignGivesWarpedHangingSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.WARPED.hangingSign().item())
        );
    }

    @GameTest(structure = "itematic:block.bamboo_wall_hanging_sign")
    public void getPickStackOnBambooWallHangingSignGivesBambooHangingSignItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.BAMBOO.hangingSign().item())
        );
    }

    @GameTest(structure = "itematic:block.attached_pumpkin_stem")
    public void getPickStackOnAttachedPumpkinStemGivesPumpkinSeedsItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.PUMPKIN_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.attached_melon_stem")
    public void getPickStackOnAttachedMelonStemGivesMelonSeedsItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.MELON_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.pumpkin_stem")
    public void getPickStackOnPumpkinStemGivesPumpkinSeedsItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.PUMPKIN_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.melon_stem")
    public void getPickStackOnMelonStemGivesMelonSeedsItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.MELON_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.cocoa")
    public void getPickStackOnCocoaGivesCocoaBeansItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.COCOA_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.carrots")
    public void getPickStackOnCarrotsGivesCarrotItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CARROT_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.potatoes")
    public void getPickStackOnPotatoesGivesPotatoItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.POTATO_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.torchflower_crop")
    public void getPickStackOnTorchflowerCropGivesTorchflowerSeedsItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.TORCHFLOWER_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.pitcher_crop")
    public void getPickStackOnPitcherCropGivesPitcherPodItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.PITCHER_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.beetroots")
    public void getPickStackOnBeetrootsGivesBeetrootSeedsItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BEETROOT_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.cave_vines")
    public void getPickStackOnCaveVinesGivesGlowBerriesItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.GLOW_BERRY_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.cave_vines_plant")
    public void getPickStackOnCaveVinesPlantGivesGlowBerriesItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.GLOW_BERRY_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.big_dripleaf_stem")
    public void getPickStackOnBigDripleafStemGivesBigDripleafItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BIG_DRIPLEAF.item())
        );
    }

    @GameTest(structure = "itematic:block.tall_seagrass")
    public void getPickStackOnTallSeagrassGivesSeagrassItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.SEAGRASS.item())
        );
    }

    @GameTest(structure = "itematic:block.kelp_plant")
    public void getPickStackOnKelpPlantGivesKelpItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.KELP.item())
        );
    }

    @GameTest(structure = "itematic:block.water_cauldron")
    public void getPickStackOnWaterCauldronGivesCauldronItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAULDRON.item())
        );
    }

    @GameTest(structure = "itematic:block.lava_cauldron")
    public void getPickStackOnLavaCauldronGivesCauldronItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAULDRON.item())
        );
    }

    @GameTest(structure = "itematic:block.powder_snow_cauldron")
    public void getPickStackOnPowderSnowCauldronGivesCauldronItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAULDRON.item())
        );
    }

    @GameTest(structure = "itematic:block.powder_snow")
    public void getPickStackOnPowderSnowGivesPowderSnowBucketItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.POWDER_SNOW.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_torchflower")
    public void getPickStackOnPottedTorchflowerGivesTorchflowerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.TORCHFLOWER.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_oak_sapling")
    public void getPickStackOnPottedOakSaplingGivesOakSaplingItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.OAK.sapling().item())
        );
    }

    @GameTest(structure = "itematic:block.potted_spruce_sapling")
    public void getPickStackOnPottedSpruceSaplingGivesSpruceSaplingItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.SPRUCE.sapling().item())
        );
    }

    @GameTest(structure = "itematic:block.potted_birch_sapling")
    public void getPickStackOnPottedBirchSaplingGivesBirchSaplingItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.BIRCH.sapling().item())
        );
    }

    @GameTest(structure = "itematic:block.potted_jungle_sapling")
    public void getPickStackOnPottedJungleSaplingGivesJungleSaplingItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.JUNGLE.sapling().item())
        );
    }

    @GameTest(structure = "itematic:block.potted_acacia_sapling")
    public void getPickStackOnPottedAcaciaSaplingGivesAcaciaSaplingItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.ACACIA.sapling().item())
        );
    }

    @GameTest(structure = "itematic:block.potted_cherry_sapling")
    public void getPickStackOnPottedCherrySaplingGivesCherrySaplingItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.CHERRY.sapling().item())
        );
    }

    @GameTest(structure = "itematic:block.potted_dark_oak_sapling")
    public void getPickStackOnPottedDarkOakSaplingGivesDarkOakSaplingItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.DARK_OAK.sapling().item())
        );
    }

    @GameTest(structure = "itematic:block.potted_mangrove_propagule")
    public void getPickStackOnPottedMangrovePropaguleGivesMangrovePropaguleItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItematicBlockItemIds.MANGROVE.sapling().item())
        );
    }

    @GameTest(structure = "itematic:block.potted_fern")
    public void getPickStackOnPottedFernGivesFernItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.FERN.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_dandelion")
    public void getPickStackOnPottedDandelionGivesDandelionItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.DANDELION.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_poppy")
    public void getPickStackOnPottedPoppyGivesPoppyItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.POPPY.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_blue_orchid")
    public void getPickStackOnPottedBlueOrchidGivesBlueOrchidItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BLUE_ORCHID.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_allium")
    public void getPickStackOnPottedAlliumGivesAlliumItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.ALLIUM.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_azure_bluet")
    public void getPickStackOnPottedAzureBluetGivesAzureBluetItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.AZURE_BLUET.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_red_tulip")
    public void getPickStackOnPottedRedTulipGivesRedTulipItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.RED_TULIP.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_orange_tulip")
    public void getPickStackOnPottedOrangeTulipGivesOrangeTulipItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.ORANGE_TULIP.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_white_tulip")
    public void getPickStackOnPottedWhiteTulipGivesWhiteTulipItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.WHITE_TULIP.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_pink_tulip")
    public void getPickStackOnPottedPinkTulipGivesPinkTulipItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.PINK_TULIP.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_oxeye_daisy")
    public void getPickStackOnPottedOxeyeDaisyGivesOxeyeDaisyItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.OXEYE_DAISY.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_cornflower")
    public void getPickStackOnPottedCornflowerGivesCornflowerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CORNFLOWER.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_lily_of_the_valley")
    public void getPickStackOnPottedLilyOfTheValleyGivesLilyOfTheValleyItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.LILY_OF_THE_VALLEY.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_wither_rose")
    public void getPickStackOnPottedWitherRoseGivesWitherRoseItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.WITHER_ROSE.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_red_mushroom")
    public void getPickStackOnPottedRedMushroomGivesRedMushroomItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.RED_MUSHROOM.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_brown_mushroom")
    public void getPickStackOnPottedBrownMushroomGivesBrownMushroomItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BROWN_MUSHROOM.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_dead_bush")
    public void getPickStackOnPottedDeadBushGivesDeadBushItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.DEAD_BUSH.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_cactus")
    public void getPickStackOnPottedCactusGivesCactusItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CACTUS.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_bamboo")
    public void getPickStackOnPottedBambooGivesBambooItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BAMBOO.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_crimson_fungus")
    public void getPickStackOnPottedCrimsonFungusGivesCrimsonFungusItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CRIMSON_FUNGUS.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_warped_fungus")
    public void getPickStackOnPottedWarpedFungusGivesWarpedFungusItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.WARPED_FUNGUS.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_crimson_roots")
    public void getPickStackOnPottedCrimsonRootsGivesCrimsonRootsItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CRIMSON_ROOTS.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_warped_roots")
    public void getPickStackOnPottedWarpedRootsGivesWarpedRootsItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.WARPED_ROOTS.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_azalea_bush")
    public void getPickStackOnPottedAzaleaBushGivesAzaleaItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.AZALEA.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_flowering_azalea_bush")
    public void getPickStackOnPottedFloweringAzaleaBushGivesFloweringAzaleaItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.FLOWERING_AZALEA.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_open_eyeblossom")
    public void getPickStackOnPottedOpenEyeblossomGivesOpenEyeblossomItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.OPEN_EYEBLOSSOM.item())
        );
    }

    @GameTest(structure = "itematic:block.potted_closed_eyeblossom")
    public void getPickStackOnPottedClosedEyeblossomGivesClosedEyeblossomItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CLOSED_EYEBLOSSOM.item())
        );
    }

    @GameTest(structure = "itematic:block.skeleton_wall_skull")
    public void getPickStackOnSkeletonWallSkullGivesSkeletonSkullItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.SKELETON_SKULL.item())
        );
    }

    @GameTest(structure = "itematic:block.wither_skeleton_wall_skull")
    public void getPickStackOnWitherSkeletonWallSkullGivesWitherSkeletonSkullItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.WITHER_SKELETON_SKULL.item())
        );
    }

    @GameTest(structure = "itematic:block.zombie_wall_head")
    public void getPickStackOnZombieWallHeadGivesZombieHeadItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.ZOMBIE_HEAD.item())
        );
    }

    @GameTest(structure = "itematic:block.player_wall_head")
    public void getPickStackOnPlayerWallHeadGivesPlayerHeadItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.PLAYER_HEAD.item())
        );
    }

    @GameTest(structure = "itematic:block.creeper_wall_head")
    public void getPickStackOnCreeperWallHeadGivesCreeperHeadItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CREEPER_HEAD.item())
        );
    }

    @GameTest(structure = "itematic:block.dragon_wall_head")
    public void getPickStackOnDragonWallHeadGivesDragonHeadItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.DRAGON_HEAD.item())
        );
    }

    @GameTest(structure = "itematic:block.piglin_wall_head")
    public void getPickStackOnPiglinWallHeadGivesPiglinHeadItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.PIGLIN_HEAD.item())
        );
    }

    @GameTest(structure = "itematic:block.white_wall_banner")
    public void getPickStackOnWhiteWallBannerGivesWhiteBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.white().item())
        );
    }

    @GameTest(structure = "itematic:block.orange_wall_banner")
    public void getPickStackOnOrangeWallBannerGivesOrangeBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.orange().item())
        );
    }

    @GameTest(structure = "itematic:block.magenta_wall_banner")
    public void getPickStackOnMagentaWallBannerGivesMagentaBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.magenta().item())
        );
    }

    @GameTest(structure = "itematic:block.light_blue_wall_banner")
    public void getPickStackOnLightBlueWallBannerGivesLightBlueBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.lightBlue().item())
        );
    }

    @GameTest(structure = "itematic:block.yellow_wall_banner")
    public void getPickStackOnYellowWallBannerGivesYellowBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.yellow().item())
        );
    }

    @GameTest(structure = "itematic:block.lime_wall_banner")
    public void getPickStackOnLimeWallBannerGivesLimeBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.lime().item())
        );
    }

    @GameTest(structure = "itematic:block.pink_wall_banner")
    public void getPickStackOnPinkWallBannerGivesPinkBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.pink().item())
        );
    }

    @GameTest(structure = "itematic:block.gray_wall_banner")
    public void getPickStackOnGrayWallBannerGivesGrayBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.gray().item())
        );
    }

    @GameTest(structure = "itematic:block.light_gray_wall_banner")
    public void getPickStackOnLightGrayWallBannerGivesLightGrayBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.lightGray().item())
        );
    }

    @GameTest(structure = "itematic:block.cyan_wall_banner")
    public void getPickStackOnCyanWallBannerGivesCyanBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.cyan().item())
        );
    }

    @GameTest(structure = "itematic:block.purple_wall_banner")
    public void getPickStackOnPurpleWallBannerGivesPurpleBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.purple().item())
        );
    }

    @GameTest(structure = "itematic:block.blue_wall_banner")
    public void getPickStackOnBlueWallBannerGivesBlueBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.blue().item())
        );
    }

    @GameTest(structure = "itematic:block.brown_wall_banner")
    public void getPickStackOnBrownWallBannerGivesBrownBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.brown().item())
        );
    }

    @GameTest(structure = "itematic:block.green_wall_banner")
    public void getPickStackOnGreenWallBannerGivesGreenBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.green().item())
        );
    }

    @GameTest(structure = "itematic:block.red_wall_banner")
    public void getPickStackOnRedWallBannerGivesRedBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.red().item())
        );
    }

    @GameTest(structure = "itematic:block.black_wall_banner")
    public void getPickStackOnBlackWallBannerGivesBlackBannerItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BANNER.black().item())
        );
    }

    @GameTest(structure = "itematic:block.dead_tube_coral_wall_fan")
    public void getPickStackOnDeadTubeCoralWallFanGivesDeadTubeCoralFanItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.DEAD_TUBE_CORAL_FAN.item())
        );
    }

    @GameTest(structure = "itematic:block.dead_brain_coral_wall_fan")
    public void getPickStackOnDeadBrainCoralWallFanGivesDeadBrainCoralFanItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.DEAD_BRAIN_CORAL_FAN.item())
        );
    }

    @GameTest(structure = "itematic:block.dead_bubble_coral_wall_fan")
    public void getPickStackOnDeadBubbleCoralWallFanGivesDeadBubbleCoralFanItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.DEAD_BUBBLE_CORAL_FAN.item())
        );
    }

    @GameTest(structure = "itematic:block.dead_fire_coral_wall_fan")
    public void getPickStackOnDeadFireCoralWallFanGivesDeadFireCoralFanItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.DEAD_FIRE_CORAL_FAN.item())
        );
    }

    @GameTest(structure = "itematic:block.dead_horn_coral_wall_fan")
    public void getPickStackOnDeadHornCoralWallFanGivesDeadHornCoralFanItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.DEAD_HORN_CORAL_FAN.item())
        );
    }

    @GameTest(structure = "itematic:block.tube_coral_wall_fan")
    public void getPickStackOnTubeCoralWallFanGivesTubeCoralFanItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.TUBE_CORAL_FAN.item())
        );
    }

    @GameTest(structure = "itematic:block.brain_coral_wall_fan")
    public void getPickStackOnBrainCoralWallFanGivesBrainCoralFanItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BRAIN_CORAL_FAN.item())
        );
    }

    @GameTest(structure = "itematic:block.bubble_coral_wall_fan")
    public void getPickStackOnBubbleCoralWallFanGivesBubbleCoralFanItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BUBBLE_CORAL_FAN.item())
        );
    }

    @GameTest(structure = "itematic:block.fire_coral_wall_fan")
    public void getPickStackOnFireCoralWallFanGivesFireCoralFanItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.FIRE_CORAL_FAN.item())
        );
    }

    @GameTest(structure = "itematic:block.horn_coral_wall_fan")
    public void getPickStackOnHornCoralWallFanGivesHornCoralFanItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.HORN_CORAL_FAN.item())
        );
    }

    @GameTest(structure = "itematic:block.bamboo_sapling")
    public void getPickStackOnBambooSaplingGivesBambooItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.BAMBOO.item())
        );
    }

    @GameTest(structure = "itematic:block.sweet_berry_bush")
    public void getPickStackOnSweetBerryBushGivesSweetBerriesItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.SWEET_BERRY_CROP.item())
        );
    }

    @GameTest(structure = "itematic:block.weeping_vines_plant")
    public void getPickStackOnWeepingVinesPlantGivesWeepingVinesItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.WEEPING_VINES.item())
        );
    }

    @GameTest(structure = "itematic:block.twisting_vines_plant")
    public void getPickStackOnTwistingVinesPlantGivesTwistingVinesItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.TWISTING_VINES.item())
        );
    }

    @GameTest(structure = "itematic:block.candle_cake")
    public void getPickStackOnCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.white_candle_cake")
    public void getPickStackOnWhiteCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.orange_candle_cake")
    public void getPickStackOnOrangeCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.magenta_candle_cake")
    public void getPickStackOnMagentaCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.light_blue_candle_cake")
    public void getPickStackOnLightBlueCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.yellow_candle_cake")
    public void getPickStackOnYellowCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.lime_candle_cake")
    public void getPickStackOnLimeCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.pink_candle_cake")
    public void getPickStackOnPinkCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.gray_candle_cake")
    public void getPickStackOnGrayCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.light_gray_candle_cake")
    public void getPickStackOnLightGrayCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.cyan_candle_cake")
    public void getPickStackOnCyanCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.purple_candle_cake")
    public void getPickStackOnPurpleCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.blue_candle_cake")
    public void getPickStackOnBlueCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.brown_candle_cake")
    public void getPickStackOnBrownCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.green_candle_cake")
    public void getPickStackOnGreenCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.red_candle_cake")
    public void getPickStackOnRedCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }

    @GameTest(structure = "itematic:block.black_candle_cake")
    public void getPickStackOnBlackCandleCakeGivesCakeItemStack(GameTestHelper helper) {
        BlockState state = helper.getBlockState(BLOCK_POSITION);
        BlockPos absolutePos = helper.absolutePos(BLOCK_POSITION);
        ItemStack stack = state.getCloneItemStack(helper.getLevel(), absolutePos, false);
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(BlockItemIds.CAKE.item())
        );
    }
}
