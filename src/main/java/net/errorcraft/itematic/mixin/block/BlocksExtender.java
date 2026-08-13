package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractPlantStemBlockAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

    @Shadow
    @Final
    public static Block POTTED_OPEN_EYEBLOSSOM;

    @Shadow
    @Final
    public static Block POTTED_CLOSED_EYEBLOSSOM;

    static {
        CAVE_AIR.itematic$setAsItemKey(ItemIds.AIR);
        VOID_AIR.itematic$setAsItemKey(ItemIds.AIR);
        WATER.itematic$setAsItemKey(ItemIds.WATER_BUCKET);
        LAVA.itematic$setAsItemKey(ItemIds.LAVA_BUCKET);
        BUBBLE_COLUMN.itematic$setAsItemKey(ItemIds.AIR);
        FROSTED_ICE.itematic$setAsItemKey(ItemIds.AIR);
        FIRE.itematic$setAsItemKey(ItemIds.AIR);
        SOUL_FIRE.itematic$setAsItemKey(ItemIds.AIR);
        NETHER_PORTAL.itematic$setAsItemKey(ItemIds.AIR);
        END_PORTAL.itematic$setAsItemKey(ItemIds.AIR);
        END_GATEWAY.itematic$setAsItemKey(ItemIds.AIR);
        MOVING_PISTON.itematic$setAsItemKey(ItemIds.AIR);
        PISTON_HEAD.itematic$setAsItemKey(ItemIds.PISTON);
        WALL_TORCH.itematic$setAsItemKey(ItemIds.TORCH);
        SOUL_WALL_TORCH.itematic$setAsItemKey(ItemIds.SOUL_TORCH);
        REDSTONE_WALL_TORCH.itematic$setAsItemKey(ItemIds.REDSTONE_TORCH);
        REDSTONE_WIRE.itematic$setAsItemKey(ItemIds.REDSTONE);
        TRIPWIRE.itematic$setAsItemKey(ItemIds.STRING);
        OAK_WALL_SIGN.itematic$setAsItemKey(ItemIds.OAK_SIGN);
        SPRUCE_WALL_SIGN.itematic$setAsItemKey(ItemIds.SPRUCE_SIGN);
        BIRCH_WALL_SIGN.itematic$setAsItemKey(ItemIds.BIRCH_SIGN);
        ACACIA_WALL_SIGN.itematic$setAsItemKey(ItemIds.ACACIA_SIGN);
        CHERRY_WALL_SIGN.itematic$setAsItemKey(ItemIds.CHERRY_SIGN);
        JUNGLE_WALL_SIGN.itematic$setAsItemKey(ItemIds.JUNGLE_SIGN);
        DARK_OAK_WALL_SIGN.itematic$setAsItemKey(ItemIds.DARK_OAK_SIGN);
        PALE_OAK_WALL_SIGN.itematic$setAsItemKey(ItemIds.PALE_OAK_SIGN);
        MANGROVE_WALL_SIGN.itematic$setAsItemKey(ItemIds.MANGROVE_SIGN);
        CRIMSON_WALL_SIGN.itematic$setAsItemKey(ItemIds.CRIMSON_SIGN);
        WARPED_WALL_SIGN.itematic$setAsItemKey(ItemIds.WARPED_SIGN);
        BAMBOO_WALL_SIGN.itematic$setAsItemKey(ItemIds.BAMBOO_SIGN);
        OAK_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemIds.OAK_HANGING_SIGN);
        SPRUCE_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemIds.SPRUCE_HANGING_SIGN);
        BIRCH_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemIds.BIRCH_HANGING_SIGN);
        ACACIA_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemIds.ACACIA_HANGING_SIGN);
        CHERRY_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemIds.CHERRY_HANGING_SIGN);
        JUNGLE_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemIds.JUNGLE_HANGING_SIGN);
        DARK_OAK_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemIds.DARK_OAK_HANGING_SIGN);
        PALE_OAK_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemIds.PALE_OAK_HANGING_SIGN);
        MANGROVE_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemIds.MANGROVE_HANGING_SIGN);
        CRIMSON_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemIds.CRIMSON_HANGING_SIGN);
        WARPED_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemIds.WARPED_HANGING_SIGN);
        BAMBOO_WALL_HANGING_SIGN.itematic$setAsItemKey(ItemIds.BAMBOO_HANGING_SIGN);
        WATER_CAULDRON.itematic$setAsItemKey(ItemIds.CAULDRON);
        LAVA_CAULDRON.itematic$setAsItemKey(ItemIds.CAULDRON);
        POWDER_SNOW_CAULDRON.itematic$setAsItemKey(ItemIds.CAULDRON);
        POWDER_SNOW.itematic$setAsItemKey(ItemIds.POWDER_SNOW_BUCKET);
        POTTED_TORCHFLOWER.itematic$setAsItemKey(ItemIds.TORCHFLOWER);
        POTTED_OAK_SAPLING.itematic$setAsItemKey(ItemIds.OAK_SAPLING);
        POTTED_SPRUCE_SAPLING.itematic$setAsItemKey(ItemIds.SPRUCE_SAPLING);
        POTTED_BIRCH_SAPLING.itematic$setAsItemKey(ItemIds.BIRCH_SAPLING);
        POTTED_JUNGLE_SAPLING.itematic$setAsItemKey(ItemIds.JUNGLE_SAPLING);
        POTTED_ACACIA_SAPLING.itematic$setAsItemKey(ItemIds.ACACIA_SAPLING);
        POTTED_CHERRY_SAPLING.itematic$setAsItemKey(ItemIds.CHERRY_SAPLING);
        POTTED_DARK_OAK_SAPLING.itematic$setAsItemKey(ItemIds.DARK_OAK_SAPLING);
        POTTED_MANGROVE_PROPAGULE.itematic$setAsItemKey(ItemIds.MANGROVE_PROPAGULE);
        POTTED_FERN.itematic$setAsItemKey(ItemIds.FERN);
        POTTED_DANDELION.itematic$setAsItemKey(ItemIds.DANDELION);
        POTTED_POPPY.itematic$setAsItemKey(ItemIds.POPPY);
        POTTED_BLUE_ORCHID.itematic$setAsItemKey(ItemIds.BLUE_ORCHID);
        POTTED_ALLIUM.itematic$setAsItemKey(ItemIds.ALLIUM);
        POTTED_AZURE_BLUET.itematic$setAsItemKey(ItemIds.AZURE_BLUET);
        POTTED_RED_TULIP.itematic$setAsItemKey(ItemIds.RED_TULIP);
        POTTED_ORANGE_TULIP.itematic$setAsItemKey(ItemIds.ORANGE_TULIP);
        POTTED_WHITE_TULIP.itematic$setAsItemKey(ItemIds.WHITE_TULIP);
        POTTED_PINK_TULIP.itematic$setAsItemKey(ItemIds.PINK_TULIP);
        POTTED_OXEYE_DAISY.itematic$setAsItemKey(ItemIds.OXEYE_DAISY);
        POTTED_CORNFLOWER.itematic$setAsItemKey(ItemIds.CORNFLOWER);
        POTTED_LILY_OF_THE_VALLEY.itematic$setAsItemKey(ItemIds.LILY_OF_THE_VALLEY);
        POTTED_WITHER_ROSE.itematic$setAsItemKey(ItemIds.WITHER_ROSE);
        POTTED_RED_MUSHROOM.itematic$setAsItemKey(ItemIds.RED_MUSHROOM);
        POTTED_BROWN_MUSHROOM.itematic$setAsItemKey(ItemIds.BROWN_MUSHROOM);
        POTTED_DEAD_BUSH.itematic$setAsItemKey(ItemIds.DEAD_BUSH);
        POTTED_CACTUS.itematic$setAsItemKey(ItemIds.CACTUS);
        POTTED_BAMBOO.itematic$setAsItemKey(ItemIds.BAMBOO);
        POTTED_CRIMSON_FUNGUS.itematic$setAsItemKey(ItemIds.CRIMSON_FUNGUS);
        POTTED_WARPED_FUNGUS.itematic$setAsItemKey(ItemIds.WARPED_FUNGUS);
        POTTED_CRIMSON_ROOTS.itematic$setAsItemKey(ItemIds.CRIMSON_ROOTS);
        POTTED_WARPED_ROOTS.itematic$setAsItemKey(ItemIds.WARPED_ROOTS);
        POTTED_AZALEA.itematic$setAsItemKey(ItemIds.AZALEA);
        POTTED_FLOWERING_AZALEA.itematic$setAsItemKey(ItemIds.FLOWERING_AZALEA);
        POTTED_OPEN_EYEBLOSSOM.itematic$setAsItemKey(ItemIds.OPEN_EYEBLOSSOM);
        POTTED_CLOSED_EYEBLOSSOM.itematic$setAsItemKey(ItemIds.CLOSED_EYEBLOSSOM);
        SKELETON_WALL_SKULL.itematic$setAsItemKey(ItemIds.SKELETON_SKULL);
        WITHER_SKELETON_WALL_SKULL.itematic$setAsItemKey(ItemIds.WITHER_SKELETON_SKULL);
        ZOMBIE_WALL_HEAD.itematic$setAsItemKey(ItemIds.ZOMBIE_HEAD);
        PLAYER_WALL_HEAD.itematic$setAsItemKey(ItemIds.PLAYER_HEAD);
        CREEPER_WALL_HEAD.itematic$setAsItemKey(ItemIds.CREEPER_HEAD);
        DRAGON_WALL_HEAD.itematic$setAsItemKey(ItemIds.DRAGON_HEAD);
        PIGLIN_WALL_HEAD.itematic$setAsItemKey(ItemIds.PIGLIN_HEAD);
        WHITE_WALL_BANNER.itematic$setAsItemKey(ItemIds.WHITE_BANNER);
        ORANGE_WALL_BANNER.itematic$setAsItemKey(ItemIds.ORANGE_BANNER);
        MAGENTA_WALL_BANNER.itematic$setAsItemKey(ItemIds.MAGENTA_BANNER);
        LIGHT_BLUE_WALL_BANNER.itematic$setAsItemKey(ItemIds.LIGHT_BLUE_BANNER);
        YELLOW_WALL_BANNER.itematic$setAsItemKey(ItemIds.YELLOW_BANNER);
        LIME_WALL_BANNER.itematic$setAsItemKey(ItemIds.LIME_BANNER);
        PINK_WALL_BANNER.itematic$setAsItemKey(ItemIds.PINK_BANNER);
        GRAY_WALL_BANNER.itematic$setAsItemKey(ItemIds.GRAY_BANNER);
        LIGHT_GRAY_WALL_BANNER.itematic$setAsItemKey(ItemIds.LIGHT_GRAY_BANNER);
        CYAN_WALL_BANNER.itematic$setAsItemKey(ItemIds.CYAN_BANNER);
        PURPLE_WALL_BANNER.itematic$setAsItemKey(ItemIds.PURPLE_BANNER);
        BLUE_WALL_BANNER.itematic$setAsItemKey(ItemIds.BLUE_BANNER);
        BROWN_WALL_BANNER.itematic$setAsItemKey(ItemIds.BROWN_BANNER);
        GREEN_WALL_BANNER.itematic$setAsItemKey(ItemIds.GREEN_BANNER);
        RED_WALL_BANNER.itematic$setAsItemKey(ItemIds.RED_BANNER);
        BLACK_WALL_BANNER.itematic$setAsItemKey(ItemIds.BLACK_BANNER);
        DEAD_TUBE_CORAL_WALL_FAN.itematic$setAsItemKey(ItemIds.DEAD_TUBE_CORAL_FAN);
        DEAD_BRAIN_CORAL_WALL_FAN.itematic$setAsItemKey(ItemIds.DEAD_BRAIN_CORAL_FAN);
        DEAD_BUBBLE_CORAL_WALL_FAN.itematic$setAsItemKey(ItemIds.DEAD_BUBBLE_CORAL_FAN);
        DEAD_FIRE_CORAL_WALL_FAN.itematic$setAsItemKey(ItemIds.DEAD_FIRE_CORAL_FAN);
        DEAD_HORN_CORAL_WALL_FAN.itematic$setAsItemKey(ItemIds.DEAD_HORN_CORAL_FAN);
        TUBE_CORAL_WALL_FAN.itematic$setAsItemKey(ItemIds.TUBE_CORAL_FAN);
        BRAIN_CORAL_WALL_FAN.itematic$setAsItemKey(ItemIds.BRAIN_CORAL_FAN);
        BUBBLE_CORAL_WALL_FAN.itematic$setAsItemKey(ItemIds.BUBBLE_CORAL_FAN);
        FIRE_CORAL_WALL_FAN.itematic$setAsItemKey(ItemIds.FIRE_CORAL_FAN);
        HORN_CORAL_WALL_FAN.itematic$setAsItemKey(ItemIds.HORN_CORAL_FAN);
        ATTACHED_PUMPKIN_STEM.itematic$setAsItemKey(ItemIds.PUMPKIN_SEEDS);
        ATTACHED_MELON_STEM.itematic$setAsItemKey(ItemIds.MELON_SEEDS);
        PUMPKIN_STEM.itematic$setAsItemKey(ItemIds.PUMPKIN_SEEDS);
        MELON_STEM.itematic$setAsItemKey(ItemIds.MELON_SEEDS);
        CARROTS.itematic$setAsItemKey(ItemIds.CARROT);
        POTATOES.itematic$setAsItemKey(ItemIds.POTATO);
        BEETROOTS.itematic$setAsItemKey(ItemIds.BEETROOT);
        COCOA.itematic$setAsItemKey(ItemIds.COCOA_BEANS);
        TORCHFLOWER_CROP.itematic$setAsItemKey(ItemIds.TORCHFLOWER_SEEDS);
        PITCHER_CROP.itematic$setAsItemKey(ItemIds.PITCHER_POD);
        TALL_SEAGRASS.itematic$setAsItemKey(ItemIds.SEAGRASS);
        KELP_PLANT.itematic$setAsItemKey(ItemIds.KELP);
        BAMBOO_SAPLING.itematic$setAsItemKey(ItemIds.BAMBOO);
        BIG_DRIPLEAF_STEM.itematic$setAsItemKey(ItemIds.BIG_DRIPLEAF);
        CAVE_VINES.itematic$setAsItemKey(ItemIds.GLOW_BERRIES);
        CAVE_VINES_PLANT.itematic$setAsItemKey(ItemIds.GLOW_BERRIES);
        SWEET_BERRY_BUSH.itematic$setAsItemKey(ItemIds.SWEET_BERRIES);
        WEEPING_VINES_PLANT.itematic$setAsItemKey(ItemIds.WEEPING_VINES);
        TWISTING_VINES_PLANT.itematic$setAsItemKey(ItemIds.TWISTING_VINES);
        CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        WHITE_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        ORANGE_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        MAGENTA_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        LIGHT_BLUE_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        YELLOW_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        LIME_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        PINK_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        GRAY_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        LIGHT_GRAY_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        CYAN_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        PURPLE_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        BLUE_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        BROWN_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        GREEN_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        RED_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);
        BLACK_CANDLE_CAKE.itematic$setAsItemKey(ItemIds.CAKE);

        ((AbstractPlantStemBlockAccess) CAVE_VINES).itematic$setStemItemKey(ItemIds.GLOW_BERRIES);
        ((AbstractPlantStemBlockAccess) KELP).itematic$setStemItemKey(ItemIds.KELP);
        ((AbstractPlantStemBlockAccess) TWISTING_VINES).itematic$setStemItemKey(ItemIds.TWISTING_VINES);
        ((AbstractPlantStemBlockAccess) WEEPING_VINES).itematic$setStemItemKey(ItemIds.WEEPING_VINES);
    }
}
