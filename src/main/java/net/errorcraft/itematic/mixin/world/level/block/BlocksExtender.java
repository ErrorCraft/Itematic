package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.references.ItematicBlockItemIds;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Blocks.class)
public class BlocksExtender {
    @Shadow
    @Final
    public static Block CAVE_AIR;

    @Shadow
    @Final
    public static Block VOID_AIR;

    @Shadow
    @Final
    public static Block WATER;

    @Shadow
    @Final
    public static Block LAVA;

    @Shadow
    @Final
    public static Block BUBBLE_COLUMN;

    @Shadow
    @Final
    public static Block FROSTED_ICE;

    @Shadow
    @Final
    public static Block FIRE;

    @Shadow
    @Final
    public static Block SOUL_FIRE;

    @Shadow
    @Final
    public static Block NETHER_PORTAL;

    @Shadow
    @Final
    public static Block END_PORTAL;

    @Shadow
    @Final
    public static Block END_GATEWAY;

    @Shadow
    @Final
    public static Block MOVING_PISTON;

    @Shadow
    @Final
    public static Block PISTON_HEAD;

    @Shadow
    @Final
    public static Block WALL_TORCH;

    @Shadow
    @Final
    public static Block SOUL_WALL_TORCH;

    @Shadow
    @Final
    public static Block REDSTONE_WALL_TORCH;

    @Shadow
    @Final
    public static Block REDSTONE_WIRE;

    @Shadow
    @Final
    public static Block TRIPWIRE;

    @Shadow
    @Final
    public static Block OAK_WALL_SIGN;

    @Shadow
    @Final
    public static Block SPRUCE_WALL_SIGN;

    @Shadow
    @Final
    public static Block BIRCH_WALL_SIGN;

    @Shadow
    @Final
    public static Block ACACIA_WALL_SIGN;

    @Shadow
    @Final
    public static Block CHERRY_WALL_SIGN;

    @Shadow
    @Final
    public static Block JUNGLE_WALL_SIGN;

    @Shadow
    @Final
    public static Block DARK_OAK_WALL_SIGN;

    @Shadow
    @Final
    public static Block PALE_OAK_WALL_SIGN;

    @Shadow
    @Final
    public static Block MANGROVE_WALL_SIGN;

    @Shadow
    @Final
    public static Block CRIMSON_WALL_SIGN;

    @Shadow
    @Final
    public static Block WARPED_WALL_SIGN;

    @Shadow
    @Final
    public static Block BAMBOO_WALL_SIGN;

    @Shadow
    @Final
    public static Block OAK_WALL_HANGING_SIGN;

    @Shadow
    @Final
    public static Block SPRUCE_WALL_HANGING_SIGN;

    @Shadow
    @Final
    public static Block BIRCH_WALL_HANGING_SIGN;

    @Shadow
    @Final
    public static Block ACACIA_WALL_HANGING_SIGN;

    @Shadow
    @Final
    public static Block CHERRY_WALL_HANGING_SIGN;

    @Shadow
    @Final
    public static Block JUNGLE_WALL_HANGING_SIGN;

    @Shadow
    @Final
    public static Block DARK_OAK_WALL_HANGING_SIGN;

    @Shadow
    @Final
    public static Block PALE_OAK_WALL_HANGING_SIGN;

    @Shadow
    @Final
    public static Block MANGROVE_WALL_HANGING_SIGN;

    @Shadow
    @Final
    public static Block CRIMSON_WALL_HANGING_SIGN;

    @Shadow
    @Final
    public static Block WARPED_WALL_HANGING_SIGN;

    @Shadow
    @Final
    public static Block BAMBOO_WALL_HANGING_SIGN;

    @Shadow
    @Final
    public static Block WATER_CAULDRON;

    @Shadow
    @Final
    public static Block LAVA_CAULDRON;

    @Shadow
    @Final
    public static Block POWDER_SNOW_CAULDRON;

    @Shadow
    @Final
    public static Block POWDER_SNOW;

    @Shadow
    @Final
    public static Block POTTED_TORCHFLOWER;

    @Shadow
    @Final
    public static Block POTTED_OAK_SAPLING;

    @Shadow
    @Final
    public static Block POTTED_SPRUCE_SAPLING;

    @Shadow
    @Final
    public static Block POTTED_BIRCH_SAPLING;

    @Shadow
    @Final
    public static Block POTTED_JUNGLE_SAPLING;

    @Shadow
    @Final
    public static Block POTTED_ACACIA_SAPLING;

    @Shadow
    @Final
    public static Block POTTED_CHERRY_SAPLING;

    @Shadow
    @Final
    public static Block POTTED_DARK_OAK_SAPLING;

    @Shadow
    @Final
    public static Block POTTED_MANGROVE_PROPAGULE;

    @Shadow
    @Final
    public static Block POTTED_FERN;

    @Shadow
    @Final
    public static Block POTTED_DANDELION;

    @Shadow
    @Final
    public static Block POTTED_POPPY;

    @Shadow
    @Final
    public static Block POTTED_BLUE_ORCHID;

    @Shadow
    @Final
    public static Block POTTED_ALLIUM;

    @Shadow
    @Final
    public static Block POTTED_AZURE_BLUET;

    @Shadow
    @Final
    public static Block POTTED_RED_TULIP;

    @Shadow
    @Final
    public static Block POTTED_ORANGE_TULIP;

    @Shadow
    @Final
    public static Block POTTED_WHITE_TULIP;

    @Shadow
    @Final
    public static Block POTTED_PINK_TULIP;

    @Shadow
    @Final
    public static Block POTTED_OXEYE_DAISY;

    @Shadow
    @Final
    public static Block POTTED_CORNFLOWER;

    @Shadow
    @Final
    public static Block POTTED_LILY_OF_THE_VALLEY;

    @Shadow
    @Final
    public static Block POTTED_WITHER_ROSE;

    @Shadow
    @Final
    public static Block POTTED_RED_MUSHROOM;

    @Shadow
    @Final
    public static Block POTTED_BROWN_MUSHROOM;

    @Shadow
    @Final
    public static Block POTTED_DEAD_BUSH;

    @Shadow
    @Final
    public static Block POTTED_CACTUS;

    @Shadow
    @Final
    public static Block POTTED_BAMBOO;

    @Shadow
    @Final
    public static Block POTTED_CRIMSON_FUNGUS;

    @Shadow
    @Final
    public static Block POTTED_WARPED_FUNGUS;

    @Shadow
    @Final
    public static Block POTTED_CRIMSON_ROOTS;

    @Shadow
    @Final
    public static Block POTTED_WARPED_ROOTS;

    @Shadow
    @Final
    public static Block POTTED_AZALEA;

    @Shadow
    @Final
    public static Block POTTED_FLOWERING_AZALEA;

    @Shadow
    @Final
    public static Block SKELETON_WALL_SKULL;

    @Shadow
    @Final
    public static Block WITHER_SKELETON_WALL_SKULL;

    @Shadow
    @Final
    public static Block ZOMBIE_WALL_HEAD;

    @Shadow
    @Final
    public static Block PLAYER_WALL_HEAD;

    @Shadow
    @Final
    public static Block CREEPER_WALL_HEAD;

    @Shadow
    @Final
    public static Block DRAGON_WALL_HEAD;

    @Shadow
    @Final
    public static Block PIGLIN_WALL_HEAD;

    @Shadow
    @Final
    public static ColorCollection<Block> WALL_BANNER;

    @Shadow
    @Final
    public static Block DEAD_TUBE_CORAL_WALL_FAN;

    @Shadow
    @Final
    public static Block DEAD_BRAIN_CORAL_WALL_FAN;

    @Shadow
    @Final
    public static Block DEAD_BUBBLE_CORAL_WALL_FAN;

    @Shadow
    @Final
    public static Block DEAD_FIRE_CORAL_WALL_FAN;

    @Shadow
    @Final
    public static Block DEAD_HORN_CORAL_WALL_FAN;

    @Shadow
    @Final
    public static Block TUBE_CORAL_WALL_FAN;

    @Shadow
    @Final
    public static Block BRAIN_CORAL_WALL_FAN;

    @Shadow
    @Final
    public static Block BUBBLE_CORAL_WALL_FAN;

    @Shadow
    @Final
    public static Block FIRE_CORAL_WALL_FAN;

    @Shadow
    @Final
    public static Block HORN_CORAL_WALL_FAN;

    @Shadow
    @Final
    public static Block ATTACHED_PUMPKIN_STEM;

    @Shadow
    @Final
    public static Block ATTACHED_MELON_STEM;

    @Shadow
    @Final
    public static Block PUMPKIN_STEM;

    @Shadow
    @Final
    public static Block MELON_STEM;

    @Shadow
    @Final
    public static Block CARROTS;

    @Shadow
    @Final
    public static Block POTATOES;

    @Shadow
    @Final
    public static Block BEETROOTS;

    @Shadow
    @Final
    public static Block COCOA;

    @Shadow
    @Final
    public static Block TORCHFLOWER_CROP;

    @Shadow
    @Final
    public static Block PITCHER_CROP;

    @Shadow
    @Final
    public static Block TALL_SEAGRASS;

    @Shadow
    @Final
    public static Block KELP;

    @Shadow
    @Final
    public static Block KELP_PLANT;

    @Shadow
    @Final
    public static Block BAMBOO_SAPLING;

    @Shadow
    @Final
    public static Block BIG_DRIPLEAF_STEM;

    @Shadow
    @Final
    public static Block CAVE_VINES;

    @Shadow
    @Final
    public static Block CAVE_VINES_PLANT;

    @Shadow
    @Final
    public static Block SWEET_BERRY_BUSH;

    @Shadow
    @Final
    public static Block WEEPING_VINES;

    @Shadow
    @Final
    public static Block WEEPING_VINES_PLANT;

    @Shadow
    @Final
    public static Block TWISTING_VINES;

    @Shadow
    @Final
    public static Block TWISTING_VINES_PLANT;

    @Shadow
    @Final
    public static Block CANDLE_CAKE;

    @Shadow
    @Final
    public static ColorCollection<Block> DYED_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block POTTED_OPEN_EYEBLOSSOM;

    @Shadow
    @Final
    public static Block POTTED_CLOSED_EYEBLOSSOM;

    static {
        CAVE_AIR.itematic$setAsItemId(BlockItemIds.AIR.item());
        VOID_AIR.itematic$setAsItemId(BlockItemIds.AIR.item());
        WATER.itematic$setAsItemId(ItemIds.WATER_BUCKET);
        LAVA.itematic$setAsItemId(ItemIds.LAVA_BUCKET);
        BUBBLE_COLUMN.itematic$setAsItemId(BlockItemIds.AIR.item());
        FROSTED_ICE.itematic$setAsItemId(BlockItemIds.AIR.item());
        FIRE.itematic$setAsItemId(BlockItemIds.AIR.item());
        SOUL_FIRE.itematic$setAsItemId(BlockItemIds.AIR.item());
        NETHER_PORTAL.itematic$setAsItemId(BlockItemIds.AIR.item());
        END_PORTAL.itematic$setAsItemId(BlockItemIds.AIR.item());
        END_GATEWAY.itematic$setAsItemId(BlockItemIds.AIR.item());
        MOVING_PISTON.itematic$setAsItemId(BlockItemIds.AIR.item());
        PISTON_HEAD.itematic$setAsItemId(BlockItemIds.PISTON.item());
        WALL_TORCH.itematic$setAsItemId(BlockItemIds.TORCH.item());
        SOUL_WALL_TORCH.itematic$setAsItemId(BlockItemIds.SOUL_TORCH.item());
        REDSTONE_WALL_TORCH.itematic$setAsItemId(BlockItemIds.REDSTONE_TORCH.item());
        REDSTONE_WIRE.itematic$setAsItemId(BlockItemIds.REDSTONE_DUST.item());
        TRIPWIRE.itematic$setAsItemId(BlockItemIds.TRIPWIRE.item());
        OAK_WALL_SIGN.itematic$setAsItemId(ItematicBlockItemIds.OAK.sign().item());
        SPRUCE_WALL_SIGN.itematic$setAsItemId(ItematicBlockItemIds.SPRUCE.sign().item());
        BIRCH_WALL_SIGN.itematic$setAsItemId(ItematicBlockItemIds.BIRCH.sign().item());
        ACACIA_WALL_SIGN.itematic$setAsItemId(ItematicBlockItemIds.ACACIA.sign().item());
        CHERRY_WALL_SIGN.itematic$setAsItemId(ItematicBlockItemIds.CHERRY.sign().item());
        JUNGLE_WALL_SIGN.itematic$setAsItemId(ItematicBlockItemIds.JUNGLE.sign().item());
        DARK_OAK_WALL_SIGN.itematic$setAsItemId(ItematicBlockItemIds.DARK_OAK.sign().item());
        PALE_OAK_WALL_SIGN.itematic$setAsItemId(ItematicBlockItemIds.PALE_OAK.sign().item());
        MANGROVE_WALL_SIGN.itematic$setAsItemId(ItematicBlockItemIds.MANGROVE.sign().item());
        CRIMSON_WALL_SIGN.itematic$setAsItemId(ItematicBlockItemIds.CRIMSON.sign().item());
        WARPED_WALL_SIGN.itematic$setAsItemId(ItematicBlockItemIds.WARPED.sign().item());
        BAMBOO_WALL_SIGN.itematic$setAsItemId(ItematicBlockItemIds.BAMBOO.sign().item());
        OAK_WALL_HANGING_SIGN.itematic$setAsItemId(ItematicBlockItemIds.OAK.hangingSign().item());
        SPRUCE_WALL_HANGING_SIGN.itematic$setAsItemId(ItematicBlockItemIds.SPRUCE.hangingSign().item());
        BIRCH_WALL_HANGING_SIGN.itematic$setAsItemId(ItematicBlockItemIds.BIRCH.hangingSign().item());
        ACACIA_WALL_HANGING_SIGN.itematic$setAsItemId(ItematicBlockItemIds.ACACIA.hangingSign().item());
        CHERRY_WALL_HANGING_SIGN.itematic$setAsItemId(ItematicBlockItemIds.CHERRY.hangingSign().item());
        JUNGLE_WALL_HANGING_SIGN.itematic$setAsItemId(ItematicBlockItemIds.JUNGLE.hangingSign().item());
        DARK_OAK_WALL_HANGING_SIGN.itematic$setAsItemId(ItematicBlockItemIds.DARK_OAK.hangingSign().item());
        PALE_OAK_WALL_HANGING_SIGN.itematic$setAsItemId(ItematicBlockItemIds.PALE_OAK.hangingSign().item());
        MANGROVE_WALL_HANGING_SIGN.itematic$setAsItemId(ItematicBlockItemIds.MANGROVE.hangingSign().item());
        CRIMSON_WALL_HANGING_SIGN.itematic$setAsItemId(ItematicBlockItemIds.CRIMSON.hangingSign().item());
        WARPED_WALL_HANGING_SIGN.itematic$setAsItemId(ItematicBlockItemIds.WARPED.hangingSign().item());
        BAMBOO_WALL_HANGING_SIGN.itematic$setAsItemId(ItematicBlockItemIds.BAMBOO.hangingSign().item());
        WATER_CAULDRON.itematic$setAsItemId(BlockItemIds.CAULDRON.item());
        LAVA_CAULDRON.itematic$setAsItemId(BlockItemIds.CAULDRON.item());
        POWDER_SNOW_CAULDRON.itematic$setAsItemId(BlockItemIds.CAULDRON.item());
        POWDER_SNOW.itematic$setAsItemId(BlockItemIds.POWDER_SNOW.item());
        POTTED_TORCHFLOWER.itematic$setAsItemId(BlockItemIds.TORCHFLOWER.item());
        POTTED_OAK_SAPLING.itematic$setAsItemId(ItematicBlockItemIds.OAK.sapling().item());
        POTTED_SPRUCE_SAPLING.itematic$setAsItemId(ItematicBlockItemIds.SPRUCE.sapling().item());
        POTTED_BIRCH_SAPLING.itematic$setAsItemId(ItematicBlockItemIds.BIRCH.sapling().item());
        POTTED_JUNGLE_SAPLING.itematic$setAsItemId(ItematicBlockItemIds.JUNGLE.sapling().item());
        POTTED_ACACIA_SAPLING.itematic$setAsItemId(ItematicBlockItemIds.ACACIA.sapling().item());
        POTTED_CHERRY_SAPLING.itematic$setAsItemId(ItematicBlockItemIds.CHERRY.sapling().item());
        POTTED_DARK_OAK_SAPLING.itematic$setAsItemId(ItematicBlockItemIds.DARK_OAK.sapling().item());
        POTTED_MANGROVE_PROPAGULE.itematic$setAsItemId(ItematicBlockItemIds.MANGROVE.sapling().item());
        POTTED_FERN.itematic$setAsItemId(BlockItemIds.FERN.item());
        POTTED_DANDELION.itematic$setAsItemId(BlockItemIds.DANDELION.item());
        POTTED_POPPY.itematic$setAsItemId(BlockItemIds.POPPY.item());
        POTTED_BLUE_ORCHID.itematic$setAsItemId(BlockItemIds.BLUE_ORCHID.item());
        POTTED_ALLIUM.itematic$setAsItemId(BlockItemIds.ALLIUM.item());
        POTTED_AZURE_BLUET.itematic$setAsItemId(BlockItemIds.AZURE_BLUET.item());
        POTTED_RED_TULIP.itematic$setAsItemId(BlockItemIds.RED_TULIP.item());
        POTTED_ORANGE_TULIP.itematic$setAsItemId(BlockItemIds.ORANGE_TULIP.item());
        POTTED_WHITE_TULIP.itematic$setAsItemId(BlockItemIds.WHITE_TULIP.item());
        POTTED_PINK_TULIP.itematic$setAsItemId(BlockItemIds.PINK_TULIP.item());
        POTTED_OXEYE_DAISY.itematic$setAsItemId(BlockItemIds.OXEYE_DAISY.item());
        POTTED_CORNFLOWER.itematic$setAsItemId(BlockItemIds.CORNFLOWER.item());
        POTTED_LILY_OF_THE_VALLEY.itematic$setAsItemId(BlockItemIds.LILY_OF_THE_VALLEY.item());
        POTTED_WITHER_ROSE.itematic$setAsItemId(BlockItemIds.WITHER_ROSE.item());
        POTTED_RED_MUSHROOM.itematic$setAsItemId(BlockItemIds.RED_MUSHROOM.item());
        POTTED_BROWN_MUSHROOM.itematic$setAsItemId(BlockItemIds.BROWN_MUSHROOM.item());
        POTTED_DEAD_BUSH.itematic$setAsItemId(BlockItemIds.DEAD_BUSH.item());
        POTTED_CACTUS.itematic$setAsItemId(BlockItemIds.CACTUS.item());
        POTTED_BAMBOO.itematic$setAsItemId(BlockItemIds.BAMBOO.item());
        POTTED_CRIMSON_FUNGUS.itematic$setAsItemId(BlockItemIds.CRIMSON_FUNGUS.item());
        POTTED_WARPED_FUNGUS.itematic$setAsItemId(BlockItemIds.WARPED_FUNGUS.item());
        POTTED_CRIMSON_ROOTS.itematic$setAsItemId(BlockItemIds.CRIMSON_ROOTS.item());
        POTTED_WARPED_ROOTS.itematic$setAsItemId(BlockItemIds.WARPED_ROOTS.item());
        POTTED_AZALEA.itematic$setAsItemId(BlockItemIds.AZALEA.item());
        POTTED_FLOWERING_AZALEA.itematic$setAsItemId(BlockItemIds.FLOWERING_AZALEA.item());
        POTTED_OPEN_EYEBLOSSOM.itematic$setAsItemId(BlockItemIds.OPEN_EYEBLOSSOM.item());
        POTTED_CLOSED_EYEBLOSSOM.itematic$setAsItemId(BlockItemIds.CLOSED_EYEBLOSSOM.item());
        SKELETON_WALL_SKULL.itematic$setAsItemId(BlockItemIds.SKELETON_SKULL.item());
        WITHER_SKELETON_WALL_SKULL.itematic$setAsItemId(BlockItemIds.WITHER_SKELETON_SKULL.item());
        ZOMBIE_WALL_HEAD.itematic$setAsItemId(BlockItemIds.ZOMBIE_HEAD.item());
        PLAYER_WALL_HEAD.itematic$setAsItemId(BlockItemIds.PLAYER_HEAD.item());
        CREEPER_WALL_HEAD.itematic$setAsItemId(BlockItemIds.CREEPER_HEAD.item());
        DRAGON_WALL_HEAD.itematic$setAsItemId(BlockItemIds.DRAGON_HEAD.item());
        PIGLIN_WALL_HEAD.itematic$setAsItemId(BlockItemIds.PIGLIN_HEAD.item());
        ColorCollection.zipApply(
            WALL_BANNER,
            BlockItemIds.BANNER,
            (wallBanner, banner) -> wallBanner.itematic$setAsItemId(banner.item())
        );
        DEAD_TUBE_CORAL_WALL_FAN.itematic$setAsItemId(ItematicBlockItemIds.TUBE_CORAL.fan().dead().item());
        DEAD_BRAIN_CORAL_WALL_FAN.itematic$setAsItemId(ItematicBlockItemIds.BRAIN_CORAL.fan().dead().item());
        DEAD_BUBBLE_CORAL_WALL_FAN.itematic$setAsItemId(ItematicBlockItemIds.BUBBLE_CORAL.fan().dead().item());
        DEAD_FIRE_CORAL_WALL_FAN.itematic$setAsItemId(ItematicBlockItemIds.FIRE_CORAL.fan().dead().item());
        DEAD_HORN_CORAL_WALL_FAN.itematic$setAsItemId(ItematicBlockItemIds.HORN_CORAL.fan().dead().item());
        TUBE_CORAL_WALL_FAN.itematic$setAsItemId(ItematicBlockItemIds.TUBE_CORAL.fan().alive().item());
        BRAIN_CORAL_WALL_FAN.itematic$setAsItemId(ItematicBlockItemIds.BRAIN_CORAL.fan().alive().item());
        BUBBLE_CORAL_WALL_FAN.itematic$setAsItemId(ItematicBlockItemIds.BUBBLE_CORAL.fan().alive().item());
        FIRE_CORAL_WALL_FAN.itematic$setAsItemId(ItematicBlockItemIds.FIRE_CORAL.fan().alive().item());
        HORN_CORAL_WALL_FAN.itematic$setAsItemId(ItematicBlockItemIds.HORN_CORAL.fan().alive().item());
        ATTACHED_PUMPKIN_STEM.itematic$setAsItemId(BlockItemIds.PUMPKIN_CROP.item());
        ATTACHED_MELON_STEM.itematic$setAsItemId(BlockItemIds.MELON_CROP.item());
        PUMPKIN_STEM.itematic$setAsItemId(BlockItemIds.PUMPKIN_CROP.item());
        MELON_STEM.itematic$setAsItemId(BlockItemIds.MELON_CROP.item());
        CARROTS.itematic$setAsItemId(BlockItemIds.CARROT_CROP.item());
        POTATOES.itematic$setAsItemId(BlockItemIds.POTATO_CROP.item());
        BEETROOTS.itematic$setAsItemId(BlockItemIds.BEETROOT_CROP.item());
        COCOA.itematic$setAsItemId(BlockItemIds.COCOA_CROP.item());
        TORCHFLOWER_CROP.itematic$setAsItemId(BlockItemIds.TORCHFLOWER_CROP.item());
        PITCHER_CROP.itematic$setAsItemId(BlockItemIds.PITCHER_CROP.item());
        TALL_SEAGRASS.itematic$setAsItemId(BlockItemIds.SEAGRASS.item());
        KELP_PLANT.itematic$setAsItemId(BlockItemIds.KELP.item());
        BAMBOO_SAPLING.itematic$setAsItemId(BlockItemIds.BAMBOO.item());
        BIG_DRIPLEAF_STEM.itematic$setAsItemId(BlockItemIds.BIG_DRIPLEAF.item());
        CAVE_VINES.itematic$setAsItemId(BlockItemIds.GLOW_BERRY_CROP.item());
        CAVE_VINES_PLANT.itematic$setAsItemId(BlockItemIds.GLOW_BERRY_CROP.item());
        SWEET_BERRY_BUSH.itematic$setAsItemId(BlockItemIds.SWEET_BERRY_CROP.item());
        WEEPING_VINES_PLANT.itematic$setAsItemId(BlockItemIds.WEEPING_VINES.item());
        TWISTING_VINES_PLANT.itematic$setAsItemId(BlockItemIds.TWISTING_VINES.item());
        CANDLE_CAKE.itematic$setAsItemId(BlockItemIds.CAKE.item());
        DYED_CANDLE_CAKE.forEach(dyedCandleCake -> dyedCandleCake.itematic$setAsItemId(BlockItemIds.CAKE.item()));

        ((GrowingPlantHeadBlock) CAVE_VINES).itematic$setStemItemId(BlockItemIds.GLOW_BERRY_CROP.item());
        ((GrowingPlantHeadBlock) KELP).itematic$setStemItemId(BlockItemIds.KELP.item());
        ((GrowingPlantHeadBlock) TWISTING_VINES).itematic$setStemItemId(BlockItemIds.TWISTING_VINES.item());
        ((GrowingPlantHeadBlock) WEEPING_VINES).itematic$setStemItemId(BlockItemIds.WEEPING_VINES.item());
    }
}
