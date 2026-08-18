package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
        CAVE_AIR.itematic$setAsItemId(ItemIds.AIR);
        VOID_AIR.itematic$setAsItemId(ItemIds.AIR);
        WATER.itematic$setAsItemId(ItemIds.WATER_BUCKET);
        LAVA.itematic$setAsItemId(ItemIds.LAVA_BUCKET);
        BUBBLE_COLUMN.itematic$setAsItemId(ItemIds.AIR);
        FROSTED_ICE.itematic$setAsItemId(ItemIds.AIR);
        FIRE.itematic$setAsItemId(ItemIds.AIR);
        SOUL_FIRE.itematic$setAsItemId(ItemIds.AIR);
        NETHER_PORTAL.itematic$setAsItemId(ItemIds.AIR);
        END_PORTAL.itematic$setAsItemId(ItemIds.AIR);
        END_GATEWAY.itematic$setAsItemId(ItemIds.AIR);
        MOVING_PISTON.itematic$setAsItemId(ItemIds.AIR);
        PISTON_HEAD.itematic$setAsItemId(ItemIds.PISTON);
        WALL_TORCH.itematic$setAsItemId(ItemIds.TORCH);
        SOUL_WALL_TORCH.itematic$setAsItemId(ItemIds.SOUL_TORCH);
        REDSTONE_WALL_TORCH.itematic$setAsItemId(ItemIds.REDSTONE_TORCH);
        REDSTONE_WIRE.itematic$setAsItemId(ItemIds.REDSTONE);
        TRIPWIRE.itematic$setAsItemId(ItemIds.STRING);
        OAK_WALL_SIGN.itematic$setAsItemId(ItemIds.OAK_SIGN);
        SPRUCE_WALL_SIGN.itematic$setAsItemId(ItemIds.SPRUCE_SIGN);
        BIRCH_WALL_SIGN.itematic$setAsItemId(ItemIds.BIRCH_SIGN);
        ACACIA_WALL_SIGN.itematic$setAsItemId(ItemIds.ACACIA_SIGN);
        CHERRY_WALL_SIGN.itematic$setAsItemId(ItemIds.CHERRY_SIGN);
        JUNGLE_WALL_SIGN.itematic$setAsItemId(ItemIds.JUNGLE_SIGN);
        DARK_OAK_WALL_SIGN.itematic$setAsItemId(ItemIds.DARK_OAK_SIGN);
        PALE_OAK_WALL_SIGN.itematic$setAsItemId(ItemIds.PALE_OAK_SIGN);
        MANGROVE_WALL_SIGN.itematic$setAsItemId(ItemIds.MANGROVE_SIGN);
        CRIMSON_WALL_SIGN.itematic$setAsItemId(ItemIds.CRIMSON_SIGN);
        WARPED_WALL_SIGN.itematic$setAsItemId(ItemIds.WARPED_SIGN);
        BAMBOO_WALL_SIGN.itematic$setAsItemId(ItemIds.BAMBOO_SIGN);
        OAK_WALL_HANGING_SIGN.itematic$setAsItemId(ItemIds.OAK_HANGING_SIGN);
        SPRUCE_WALL_HANGING_SIGN.itematic$setAsItemId(ItemIds.SPRUCE_HANGING_SIGN);
        BIRCH_WALL_HANGING_SIGN.itematic$setAsItemId(ItemIds.BIRCH_HANGING_SIGN);
        ACACIA_WALL_HANGING_SIGN.itematic$setAsItemId(ItemIds.ACACIA_HANGING_SIGN);
        CHERRY_WALL_HANGING_SIGN.itematic$setAsItemId(ItemIds.CHERRY_HANGING_SIGN);
        JUNGLE_WALL_HANGING_SIGN.itematic$setAsItemId(ItemIds.JUNGLE_HANGING_SIGN);
        DARK_OAK_WALL_HANGING_SIGN.itematic$setAsItemId(ItemIds.DARK_OAK_HANGING_SIGN);
        PALE_OAK_WALL_HANGING_SIGN.itematic$setAsItemId(ItemIds.PALE_OAK_HANGING_SIGN);
        MANGROVE_WALL_HANGING_SIGN.itematic$setAsItemId(ItemIds.MANGROVE_HANGING_SIGN);
        CRIMSON_WALL_HANGING_SIGN.itematic$setAsItemId(ItemIds.CRIMSON_HANGING_SIGN);
        WARPED_WALL_HANGING_SIGN.itematic$setAsItemId(ItemIds.WARPED_HANGING_SIGN);
        BAMBOO_WALL_HANGING_SIGN.itematic$setAsItemId(ItemIds.BAMBOO_HANGING_SIGN);
        WATER_CAULDRON.itematic$setAsItemId(ItemIds.CAULDRON);
        LAVA_CAULDRON.itematic$setAsItemId(ItemIds.CAULDRON);
        POWDER_SNOW_CAULDRON.itematic$setAsItemId(ItemIds.CAULDRON);
        POWDER_SNOW.itematic$setAsItemId(ItemIds.POWDER_SNOW_BUCKET);
        POTTED_TORCHFLOWER.itematic$setAsItemId(ItemIds.TORCHFLOWER);
        POTTED_OAK_SAPLING.itematic$setAsItemId(ItemIds.OAK_SAPLING);
        POTTED_SPRUCE_SAPLING.itematic$setAsItemId(ItemIds.SPRUCE_SAPLING);
        POTTED_BIRCH_SAPLING.itematic$setAsItemId(ItemIds.BIRCH_SAPLING);
        POTTED_JUNGLE_SAPLING.itematic$setAsItemId(ItemIds.JUNGLE_SAPLING);
        POTTED_ACACIA_SAPLING.itematic$setAsItemId(ItemIds.ACACIA_SAPLING);
        POTTED_CHERRY_SAPLING.itematic$setAsItemId(ItemIds.CHERRY_SAPLING);
        POTTED_DARK_OAK_SAPLING.itematic$setAsItemId(ItemIds.DARK_OAK_SAPLING);
        POTTED_MANGROVE_PROPAGULE.itematic$setAsItemId(ItemIds.MANGROVE_PROPAGULE);
        POTTED_FERN.itematic$setAsItemId(ItemIds.FERN);
        POTTED_DANDELION.itematic$setAsItemId(ItemIds.DANDELION);
        POTTED_POPPY.itematic$setAsItemId(ItemIds.POPPY);
        POTTED_BLUE_ORCHID.itematic$setAsItemId(ItemIds.BLUE_ORCHID);
        POTTED_ALLIUM.itematic$setAsItemId(ItemIds.ALLIUM);
        POTTED_AZURE_BLUET.itematic$setAsItemId(ItemIds.AZURE_BLUET);
        POTTED_RED_TULIP.itematic$setAsItemId(ItemIds.RED_TULIP);
        POTTED_ORANGE_TULIP.itematic$setAsItemId(ItemIds.ORANGE_TULIP);
        POTTED_WHITE_TULIP.itematic$setAsItemId(ItemIds.WHITE_TULIP);
        POTTED_PINK_TULIP.itematic$setAsItemId(ItemIds.PINK_TULIP);
        POTTED_OXEYE_DAISY.itematic$setAsItemId(ItemIds.OXEYE_DAISY);
        POTTED_CORNFLOWER.itematic$setAsItemId(ItemIds.CORNFLOWER);
        POTTED_LILY_OF_THE_VALLEY.itematic$setAsItemId(ItemIds.LILY_OF_THE_VALLEY);
        POTTED_WITHER_ROSE.itematic$setAsItemId(ItemIds.WITHER_ROSE);
        POTTED_RED_MUSHROOM.itematic$setAsItemId(ItemIds.RED_MUSHROOM);
        POTTED_BROWN_MUSHROOM.itematic$setAsItemId(ItemIds.BROWN_MUSHROOM);
        POTTED_DEAD_BUSH.itematic$setAsItemId(ItemIds.DEAD_BUSH);
        POTTED_CACTUS.itematic$setAsItemId(ItemIds.CACTUS);
        POTTED_BAMBOO.itematic$setAsItemId(ItemIds.BAMBOO);
        POTTED_CRIMSON_FUNGUS.itematic$setAsItemId(ItemIds.CRIMSON_FUNGUS);
        POTTED_WARPED_FUNGUS.itematic$setAsItemId(ItemIds.WARPED_FUNGUS);
        POTTED_CRIMSON_ROOTS.itematic$setAsItemId(ItemIds.CRIMSON_ROOTS);
        POTTED_WARPED_ROOTS.itematic$setAsItemId(ItemIds.WARPED_ROOTS);
        POTTED_AZALEA.itematic$setAsItemId(ItemIds.AZALEA);
        POTTED_FLOWERING_AZALEA.itematic$setAsItemId(ItemIds.FLOWERING_AZALEA);
        POTTED_OPEN_EYEBLOSSOM.itematic$setAsItemId(ItemIds.OPEN_EYEBLOSSOM);
        POTTED_CLOSED_EYEBLOSSOM.itematic$setAsItemId(ItemIds.CLOSED_EYEBLOSSOM);
        SKELETON_WALL_SKULL.itematic$setAsItemId(ItemIds.SKELETON_SKULL);
        WITHER_SKELETON_WALL_SKULL.itematic$setAsItemId(ItemIds.WITHER_SKELETON_SKULL);
        ZOMBIE_WALL_HEAD.itematic$setAsItemId(ItemIds.ZOMBIE_HEAD);
        PLAYER_WALL_HEAD.itematic$setAsItemId(ItemIds.PLAYER_HEAD);
        CREEPER_WALL_HEAD.itematic$setAsItemId(ItemIds.CREEPER_HEAD);
        DRAGON_WALL_HEAD.itematic$setAsItemId(ItemIds.DRAGON_HEAD);
        PIGLIN_WALL_HEAD.itematic$setAsItemId(ItemIds.PIGLIN_HEAD);
        WHITE_WALL_BANNER.itematic$setAsItemId(ItemIds.WHITE_BANNER);
        ORANGE_WALL_BANNER.itematic$setAsItemId(ItemIds.ORANGE_BANNER);
        MAGENTA_WALL_BANNER.itematic$setAsItemId(ItemIds.MAGENTA_BANNER);
        LIGHT_BLUE_WALL_BANNER.itematic$setAsItemId(ItemIds.LIGHT_BLUE_BANNER);
        YELLOW_WALL_BANNER.itematic$setAsItemId(ItemIds.YELLOW_BANNER);
        LIME_WALL_BANNER.itematic$setAsItemId(ItemIds.LIME_BANNER);
        PINK_WALL_BANNER.itematic$setAsItemId(ItemIds.PINK_BANNER);
        GRAY_WALL_BANNER.itematic$setAsItemId(ItemIds.GRAY_BANNER);
        LIGHT_GRAY_WALL_BANNER.itematic$setAsItemId(ItemIds.LIGHT_GRAY_BANNER);
        CYAN_WALL_BANNER.itematic$setAsItemId(ItemIds.CYAN_BANNER);
        PURPLE_WALL_BANNER.itematic$setAsItemId(ItemIds.PURPLE_BANNER);
        BLUE_WALL_BANNER.itematic$setAsItemId(ItemIds.BLUE_BANNER);
        BROWN_WALL_BANNER.itematic$setAsItemId(ItemIds.BROWN_BANNER);
        GREEN_WALL_BANNER.itematic$setAsItemId(ItemIds.GREEN_BANNER);
        RED_WALL_BANNER.itematic$setAsItemId(ItemIds.RED_BANNER);
        BLACK_WALL_BANNER.itematic$setAsItemId(ItemIds.BLACK_BANNER);
        DEAD_TUBE_CORAL_WALL_FAN.itematic$setAsItemId(ItemIds.DEAD_TUBE_CORAL_FAN);
        DEAD_BRAIN_CORAL_WALL_FAN.itematic$setAsItemId(ItemIds.DEAD_BRAIN_CORAL_FAN);
        DEAD_BUBBLE_CORAL_WALL_FAN.itematic$setAsItemId(ItemIds.DEAD_BUBBLE_CORAL_FAN);
        DEAD_FIRE_CORAL_WALL_FAN.itematic$setAsItemId(ItemIds.DEAD_FIRE_CORAL_FAN);
        DEAD_HORN_CORAL_WALL_FAN.itematic$setAsItemId(ItemIds.DEAD_HORN_CORAL_FAN);
        TUBE_CORAL_WALL_FAN.itematic$setAsItemId(ItemIds.TUBE_CORAL_FAN);
        BRAIN_CORAL_WALL_FAN.itematic$setAsItemId(ItemIds.BRAIN_CORAL_FAN);
        BUBBLE_CORAL_WALL_FAN.itematic$setAsItemId(ItemIds.BUBBLE_CORAL_FAN);
        FIRE_CORAL_WALL_FAN.itematic$setAsItemId(ItemIds.FIRE_CORAL_FAN);
        HORN_CORAL_WALL_FAN.itematic$setAsItemId(ItemIds.HORN_CORAL_FAN);
        ATTACHED_PUMPKIN_STEM.itematic$setAsItemId(ItemIds.PUMPKIN_SEEDS);
        ATTACHED_MELON_STEM.itematic$setAsItemId(ItemIds.MELON_SEEDS);
        PUMPKIN_STEM.itematic$setAsItemId(ItemIds.PUMPKIN_SEEDS);
        MELON_STEM.itematic$setAsItemId(ItemIds.MELON_SEEDS);
        CARROTS.itematic$setAsItemId(ItemIds.CARROT);
        POTATOES.itematic$setAsItemId(ItemIds.POTATO);
        BEETROOTS.itematic$setAsItemId(ItemIds.BEETROOT);
        COCOA.itematic$setAsItemId(ItemIds.COCOA_BEANS);
        TORCHFLOWER_CROP.itematic$setAsItemId(ItemIds.TORCHFLOWER_SEEDS);
        PITCHER_CROP.itematic$setAsItemId(ItemIds.PITCHER_POD);
        TALL_SEAGRASS.itematic$setAsItemId(ItemIds.SEAGRASS);
        KELP_PLANT.itematic$setAsItemId(ItemIds.KELP);
        BAMBOO_SAPLING.itematic$setAsItemId(ItemIds.BAMBOO);
        BIG_DRIPLEAF_STEM.itematic$setAsItemId(ItemIds.BIG_DRIPLEAF);
        CAVE_VINES.itematic$setAsItemId(ItemIds.GLOW_BERRIES);
        CAVE_VINES_PLANT.itematic$setAsItemId(ItemIds.GLOW_BERRIES);
        SWEET_BERRY_BUSH.itematic$setAsItemId(ItemIds.SWEET_BERRIES);
        WEEPING_VINES_PLANT.itematic$setAsItemId(ItemIds.WEEPING_VINES);
        TWISTING_VINES_PLANT.itematic$setAsItemId(ItemIds.TWISTING_VINES);
        CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        WHITE_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        ORANGE_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        MAGENTA_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        LIGHT_BLUE_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        YELLOW_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        LIME_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        PINK_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        GRAY_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        LIGHT_GRAY_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        CYAN_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        PURPLE_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        BLUE_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        BROWN_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        GREEN_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        RED_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);
        BLACK_CANDLE_CAKE.itematic$setAsItemId(ItemIds.CAKE);

        ((GrowingPlantHeadBlock) CAVE_VINES).itematic$setStemItemId(ItemIds.GLOW_BERRIES);
        ((GrowingPlantHeadBlock) KELP).itematic$setStemItemId(ItemIds.KELP);
        ((GrowingPlantHeadBlock) TWISTING_VINES).itematic$setStemItemId(ItemIds.TWISTING_VINES);
        ((GrowingPlantHeadBlock) WEEPING_VINES).itematic$setStemItemId(ItemIds.WEEPING_VINES);
    }
}
