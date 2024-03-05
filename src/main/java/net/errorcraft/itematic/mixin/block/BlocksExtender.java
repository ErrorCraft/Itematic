package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractPlantStemBlockAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
    public static Block POTTED_AZALEA_BUSH;

    @Shadow
    @Final
    public static Block POTTED_FLOWERING_AZALEA_BUSH;

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
    public static Block WHITE_WALL_BANNER;

    @Shadow
    @Final
    public static Block ORANGE_WALL_BANNER;

    @Shadow
    @Final
    public static Block MAGENTA_WALL_BANNER;

    @Shadow
    @Final
    public static Block LIGHT_BLUE_WALL_BANNER;

    @Shadow
    @Final
    public static Block YELLOW_WALL_BANNER;

    @Shadow
    @Final
    public static Block LIME_WALL_BANNER;

    @Shadow
    @Final
    public static Block PINK_WALL_BANNER;

    @Shadow
    @Final
    public static Block GRAY_WALL_BANNER;

    @Shadow
    @Final
    public static Block LIGHT_GRAY_WALL_BANNER;

    @Shadow
    @Final
    public static Block CYAN_WALL_BANNER;

    @Shadow
    @Final
    public static Block PURPLE_WALL_BANNER;

    @Shadow
    @Final
    public static Block BLUE_WALL_BANNER;

    @Shadow
    @Final
    public static Block BROWN_WALL_BANNER;

    @Shadow
    @Final
    public static Block GREEN_WALL_BANNER;

    @Shadow
    @Final
    public static Block RED_WALL_BANNER;

    @Shadow
    @Final
    public static Block BLACK_WALL_BANNER;

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
    public static Block WHITE_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block ORANGE_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block MAGENTA_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block LIGHT_BLUE_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block YELLOW_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block LIME_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block PINK_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block GRAY_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block LIGHT_GRAY_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block CYAN_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block PURPLE_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block BLUE_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block BROWN_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block GREEN_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block RED_CANDLE_CAKE;

    @Shadow
    @Final
    public static Block BLACK_CANDLE_CAKE;

    static {
        CAVE_AIR.itematic$setAsItemKey(ItemKeys.AIR);
        VOID_AIR.itematic$setAsItemKey(ItemKeys.AIR);
        WATER.itematic$setAsItemKey(ItemKeys.WATER_BUCKET);
        LAVA.itematic$setAsItemKey(ItemKeys.LAVA_BUCKET);
        BUBBLE_COLUMN.itematic$setAsItemKey(ItemKeys.AIR);
        FROSTED_ICE.itematic$setAsItemKey(ItemKeys.AIR);
        FIRE.itematic$setAsItemKey(ItemKeys.AIR);
        SOUL_FIRE.itematic$setAsItemKey(ItemKeys.AIR);
        NETHER_PORTAL.itematic$setAsItemKey(ItemKeys.AIR);
        END_PORTAL.itematic$setAsItemKey(ItemKeys.AIR);
        END_GATEWAY.itematic$setAsItemKey(ItemKeys.AIR);
        MOVING_PISTON.itematic$setAsItemKey(ItemKeys.AIR);
        PISTON_HEAD.itematic$setAsItemKey(ItemKeys.PISTON);
        WALL_TORCH.itematic$setAsItemKey(ItemKeys.TORCH);
        SOUL_WALL_TORCH.itematic$setAsItemKey(ItemKeys.SOUL_TORCH);
        REDSTONE_WALL_TORCH.itematic$setAsItemKey(ItemKeys.REDSTONE_TORCH);
        REDSTONE_WIRE.itematic$setAsItemKey(ItemKeys.REDSTONE);
        TRIPWIRE.itematic$setAsItemKey(ItemKeys.STRING);
        OAK_WALL_SIGN.itematic$setAsItemKey(ItemKeys.OAK_SIGN);
        SPRUCE_WALL_SIGN.itematic$setAsItemKey(ItemKeys.SPRUCE_SIGN);
        BIRCH_WALL_SIGN.itematic$setAsItemKey(ItemKeys.BIRCH_SIGN);
        ACACIA_WALL_SIGN.itematic$setAsItemKey(ItemKeys.ACACIA_SIGN);
        CHERRY_WALL_SIGN.itematic$setAsItemKey(ItemKeys.CHERRY_SIGN);
        JUNGLE_WALL_SIGN.itematic$setAsItemKey(ItemKeys.JUNGLE_SIGN);
        DARK_OAK_WALL_SIGN.itematic$setAsItemKey(ItemKeys.DARK_OAK_SIGN);
        MANGROVE_WALL_SIGN.itematic$setAsItemKey(ItemKeys.MANGROVE_SIGN);
        CRIMSON_WALL_SIGN.itematic$setAsItemKey(ItemKeys.CRIMSON_SIGN);
        WARPED_WALL_SIGN.itematic$setAsItemKey(ItemKeys.WARPED_SIGN);
        BAMBOO_WALL_SIGN.itematic$setAsItemKey(ItemKeys.BAMBOO_SIGN);
        OAK_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemKeys.OAK_HANGING_SIGN);
        SPRUCE_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemKeys.SPRUCE_HANGING_SIGN);
        BIRCH_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemKeys.BIRCH_HANGING_SIGN);
        ACACIA_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemKeys.ACACIA_HANGING_SIGN);
        CHERRY_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemKeys.CHERRY_HANGING_SIGN);
        JUNGLE_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemKeys.JUNGLE_HANGING_SIGN);
        DARK_OAK_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemKeys.DARK_OAK_HANGING_SIGN);
        MANGROVE_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemKeys.MANGROVE_HANGING_SIGN);
        CRIMSON_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemKeys.CRIMSON_HANGING_SIGN);
        WARPED_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemKeys.WARPED_HANGING_SIGN);
        BAMBOO_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemKeys.BAMBOO_HANGING_SIGN);
        WATER_CAULDRON.itematic$setAsItemKey(ItemKeys.CAULDRON);
        LAVA_CAULDRON.itematic$setAsItemKey(ItemKeys.CAULDRON);
        POWDER_SNOW_CAULDRON.itematic$setAsItemKey(ItemKeys.CAULDRON);
        POWDER_SNOW.itematic$setAsItemKey(ItemKeys.POWDER_SNOW_BUCKET);
        POTTED_TORCHFLOWER.itematic$setAsItemKey(ItemKeys.TORCHFLOWER);
        POTTED_OAK_SAPLING.itematic$setAsItemKey(ItemKeys.OAK_SAPLING);
        POTTED_SPRUCE_SAPLING.itematic$setAsItemKey(ItemKeys.SPRUCE_SAPLING);
        POTTED_BIRCH_SAPLING.itematic$setAsItemKey(ItemKeys.BIRCH_SAPLING);
        POTTED_JUNGLE_SAPLING.itematic$setAsItemKey(ItemKeys.JUNGLE_SAPLING);
        POTTED_ACACIA_SAPLING.itematic$setAsItemKey(ItemKeys.ACACIA_SAPLING);
        POTTED_CHERRY_SAPLING.itematic$setAsItemKey(ItemKeys.CHERRY_SAPLING);
        POTTED_DARK_OAK_SAPLING.itematic$setAsItemKey(ItemKeys.DARK_OAK_SAPLING);
        POTTED_MANGROVE_PROPAGULE.itematic$setAsItemKey(ItemKeys.MANGROVE_PROPAGULE);
        POTTED_FERN.itematic$setAsItemKey(ItemKeys.FERN);
        POTTED_DANDELION.itematic$setAsItemKey(ItemKeys.DANDELION);
        POTTED_POPPY.itematic$setAsItemKey(ItemKeys.POPPY);
        POTTED_BLUE_ORCHID.itematic$setAsItemKey(ItemKeys.BLUE_ORCHID);
        POTTED_ALLIUM.itematic$setAsItemKey(ItemKeys.ALLIUM);
        POTTED_AZURE_BLUET.itematic$setAsItemKey(ItemKeys.AZURE_BLUET);
        POTTED_RED_TULIP.itematic$setAsItemKey(ItemKeys.RED_TULIP);
        POTTED_ORANGE_TULIP.itematic$setAsItemKey(ItemKeys.ORANGE_TULIP);
        POTTED_WHITE_TULIP.itematic$setAsItemKey(ItemKeys.WHITE_TULIP);
        POTTED_PINK_TULIP.itematic$setAsItemKey(ItemKeys.PINK_TULIP);
        POTTED_OXEYE_DAISY.itematic$setAsItemKey(ItemKeys.OXEYE_DAISY);
        POTTED_CORNFLOWER.itematic$setAsItemKey(ItemKeys.CORNFLOWER);
        POTTED_LILY_OF_THE_VALLEY.itematic$setAsItemKey(ItemKeys.LILY_OF_THE_VALLEY);
        POTTED_WITHER_ROSE.itematic$setAsItemKey(ItemKeys.WITHER_ROSE);
        POTTED_RED_MUSHROOM.itematic$setAsItemKey(ItemKeys.RED_MUSHROOM);
        POTTED_BROWN_MUSHROOM.itematic$setAsItemKey(ItemKeys.BROWN_MUSHROOM);
        POTTED_DEAD_BUSH.itematic$setAsItemKey(ItemKeys.DEAD_BUSH);
        POTTED_CACTUS.itematic$setAsItemKey(ItemKeys.CACTUS);
        POTTED_BAMBOO.itematic$setAsItemKey(ItemKeys.BAMBOO);
        POTTED_CRIMSON_FUNGUS.itematic$setAsItemKey(ItemKeys.CRIMSON_FUNGUS);
        POTTED_WARPED_FUNGUS.itematic$setAsItemKey(ItemKeys.WARPED_FUNGUS);
        POTTED_CRIMSON_ROOTS.itematic$setAsItemKey(ItemKeys.CRIMSON_ROOTS);
        POTTED_WARPED_ROOTS.itematic$setAsItemKey(ItemKeys.WARPED_ROOTS);
        POTTED_AZALEA_BUSH.itematic$setAsItemKey(ItemKeys.AZALEA);
        POTTED_FLOWERING_AZALEA_BUSH.itematic$setAsItemKey(ItemKeys.FLOWERING_AZALEA);
        SKELETON_WALL_SKULL.itematic$setAsItemKey(ItemKeys.SKELETON_SKULL);
        WITHER_SKELETON_WALL_SKULL.itematic$setAsItemKey(ItemKeys.WITHER_SKELETON_SKULL);
        ZOMBIE_WALL_HEAD.itematic$setAsItemKey(ItemKeys.ZOMBIE_HEAD);
        PLAYER_WALL_HEAD.itematic$setAsItemKey(ItemKeys.PLAYER_HEAD);
        CREEPER_WALL_HEAD.itematic$setAsItemKey(ItemKeys.CREEPER_HEAD);
        DRAGON_WALL_HEAD.itematic$setAsItemKey(ItemKeys.DRAGON_HEAD);
        PIGLIN_WALL_HEAD.itematic$setAsItemKey(ItemKeys.PIGLIN_HEAD);
        WHITE_WALL_BANNER.itematic$setAsItemKey(ItemKeys.WHITE_BANNER);
        ORANGE_WALL_BANNER.itematic$setAsItemKey(ItemKeys.ORANGE_BANNER);
        MAGENTA_WALL_BANNER.itematic$setAsItemKey(ItemKeys.MAGENTA_BANNER);
        LIGHT_BLUE_WALL_BANNER.itematic$setAsItemKey(ItemKeys.LIGHT_BLUE_BANNER);
        YELLOW_WALL_BANNER.itematic$setAsItemKey(ItemKeys.YELLOW_BANNER);
        LIME_WALL_BANNER.itematic$setAsItemKey(ItemKeys.LIME_BANNER);
        PINK_WALL_BANNER.itematic$setAsItemKey(ItemKeys.PINK_BANNER);
        GRAY_WALL_BANNER.itematic$setAsItemKey(ItemKeys.GRAY_BANNER);
        LIGHT_GRAY_WALL_BANNER.itematic$setAsItemKey(ItemKeys.LIGHT_GRAY_BANNER);
        CYAN_WALL_BANNER.itematic$setAsItemKey(ItemKeys.CYAN_BANNER);
        PURPLE_WALL_BANNER.itematic$setAsItemKey(ItemKeys.PURPLE_BANNER);
        BLUE_WALL_BANNER.itematic$setAsItemKey(ItemKeys.BLUE_BANNER);
        BROWN_WALL_BANNER.itematic$setAsItemKey(ItemKeys.BROWN_BANNER);
        GREEN_WALL_BANNER.itematic$setAsItemKey(ItemKeys.GREEN_BANNER);
        RED_WALL_BANNER.itematic$setAsItemKey(ItemKeys.RED_BANNER);
        BLACK_WALL_BANNER.itematic$setAsItemKey(ItemKeys.BLACK_BANNER);
        DEAD_TUBE_CORAL_WALL_FAN.itematic$setAsItemKey(ItemKeys.DEAD_TUBE_CORAL_FAN);
        DEAD_BRAIN_CORAL_WALL_FAN.itematic$setAsItemKey(ItemKeys.DEAD_BRAIN_CORAL_FAN);
        DEAD_BUBBLE_CORAL_WALL_FAN.itematic$setAsItemKey(ItemKeys.DEAD_BUBBLE_CORAL_FAN);
        DEAD_FIRE_CORAL_WALL_FAN.itematic$setAsItemKey(ItemKeys.DEAD_FIRE_CORAL_FAN);
        DEAD_HORN_CORAL_WALL_FAN.itematic$setAsItemKey(ItemKeys.DEAD_HORN_CORAL_FAN);
        TUBE_CORAL_WALL_FAN.itematic$setAsItemKey(ItemKeys.TUBE_CORAL_FAN);
        BRAIN_CORAL_WALL_FAN.itematic$setAsItemKey(ItemKeys.BRAIN_CORAL_FAN);
        BUBBLE_CORAL_WALL_FAN.itematic$setAsItemKey(ItemKeys.BUBBLE_CORAL_FAN);
        FIRE_CORAL_WALL_FAN.itematic$setAsItemKey(ItemKeys.FIRE_CORAL_FAN);
        HORN_CORAL_WALL_FAN.itematic$setAsItemKey(ItemKeys.HORN_CORAL_FAN);
        ATTACHED_PUMPKIN_STEM.itematic$setAsItemKey(ItemKeys.PUMPKIN_SEEDS);
        ATTACHED_MELON_STEM.itematic$setAsItemKey(ItemKeys.MELON_SEEDS);
        PUMPKIN_STEM.itematic$setAsItemKey(ItemKeys.PUMPKIN_SEEDS);
        MELON_STEM.itematic$setAsItemKey(ItemKeys.MELON_SEEDS);
        CARROTS.itematic$setAsItemKey(ItemKeys.CARROT);
        POTATOES.itematic$setAsItemKey(ItemKeys.POTATO);
        BEETROOTS.itematic$setAsItemKey(ItemKeys.BEETROOT);
        COCOA.itematic$setAsItemKey(ItemKeys.COCOA_BEANS);
        TORCHFLOWER_CROP.itematic$setAsItemKey(ItemKeys.TORCHFLOWER_SEEDS);
        PITCHER_CROP.itematic$setAsItemKey(ItemKeys.PITCHER_POD);
        TALL_SEAGRASS.itematic$setAsItemKey(ItemKeys.SEAGRASS);
        KELP_PLANT.itematic$setAsItemKey(ItemKeys.KELP);
        BAMBOO_SAPLING.itematic$setAsItemKey(ItemKeys.BAMBOO);
        BIG_DRIPLEAF_STEM.itematic$setAsItemKey(ItemKeys.BIG_DRIPLEAF);
        CAVE_VINES.itematic$setAsItemKey(ItemKeys.GLOW_BERRIES);
        CAVE_VINES_PLANT.itematic$setAsItemKey(ItemKeys.GLOW_BERRIES);
        SWEET_BERRY_BUSH.itematic$setAsItemKey(ItemKeys.SWEET_BERRIES);
        WEEPING_VINES_PLANT.itematic$setAsItemKey(ItemKeys.WEEPING_VINES);
        TWISTING_VINES_PLANT.itematic$setAsItemKey(ItemKeys.TWISTING_VINES);
        CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        WHITE_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        ORANGE_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        MAGENTA_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        LIGHT_BLUE_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        YELLOW_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        LIME_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        PINK_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        GRAY_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        LIGHT_GRAY_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        CYAN_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        PURPLE_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        BLUE_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        BROWN_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        GREEN_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        RED_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);
        BLACK_CANDLE_CAKE.itematic$setAsItemKey(ItemKeys.CAKE);

        ((AbstractPlantStemBlockAccess) CAVE_VINES).itematic$setStemItemKey(ItemKeys.GLOW_BERRIES);
        ((AbstractPlantStemBlockAccess) KELP).itematic$setStemItemKey(ItemKeys.KELP);
        ((AbstractPlantStemBlockAccess) TWISTING_VINES).itematic$setStemItemKey(ItemKeys.TWISTING_VINES);
        ((AbstractPlantStemBlockAccess) WEEPING_VINES).itematic$setStemItemKey(ItemKeys.WEEPING_VINES);
    }
}
