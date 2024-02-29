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
    public static Block WATER;

    @Shadow
    @Final
    public static Block LAVA;

    @Shadow
    @Final
    public static Block WALL_TORCH;

    @Shadow
    @Final
    public static Block REDSTONE_WIRE;

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
    public static Block REDSTONE_WALL_TORCH;

    @Shadow
    @Final
    public static Block SOUL_WALL_TORCH;

    @Shadow
    @Final
    public static Block PUMPKIN_STEM;

    @Shadow
    @Final
    public static Block MELON_STEM;

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
    public static Block COCOA;

    @Shadow
    @Final
    public static Block TRIPWIRE;

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
    public static Block CARROTS;

    @Shadow
    @Final
    public static Block POTATOES;

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
    public static Block TORCHFLOWER_CROP;

    @Shadow
    @Final
    public static Block PITCHER_CROP;

    @Shadow
    @Final
    public static Block BEETROOTS;

    @Shadow
    @Final
    public static Block KELP;

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
    public static Block POTTED_BAMBOO;

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
    public static Block CRIMSON_WALL_SIGN;

    @Shadow
    @Final
    public static Block WARPED_WALL_SIGN;

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
    public static Block POWDER_SNOW;

    @Shadow
    @Final
    public static Block CAVE_VINES;

    @Shadow
    @Final
    public static Block POTTED_AZALEA_BUSH;

    @Shadow
    @Final
    public static Block POTTED_FLOWERING_AZALEA_BUSH;

    static {
        WATER.itematic$setPickBlockKey(ItemKeys.WATER_BUCKET);
        LAVA.itematic$setPickBlockKey(ItemKeys.LAVA_BUCKET);
        WALL_TORCH.itematic$setPickBlockKey(ItemKeys.TORCH);
        REDSTONE_WIRE.itematic$setPickBlockKey(ItemKeys.REDSTONE);
        OAK_WALL_SIGN.itematic$setPickBlockKey(ItemKeys.OAK_SIGN);
        SPRUCE_WALL_SIGN.itematic$setPickBlockKey(ItemKeys.SPRUCE_SIGN);
        BIRCH_WALL_SIGN.itematic$setPickBlockKey(ItemKeys.BIRCH_SIGN);
        ACACIA_WALL_SIGN.itematic$setPickBlockKey(ItemKeys.ACACIA_SIGN);
        CHERRY_WALL_SIGN.itematic$setPickBlockKey(ItemKeys.CHERRY_SIGN);
        JUNGLE_WALL_SIGN.itematic$setPickBlockKey(ItemKeys.JUNGLE_SIGN);
        DARK_OAK_WALL_SIGN.itematic$setPickBlockKey(ItemKeys.DARK_OAK_SIGN);
        MANGROVE_WALL_SIGN.itematic$setPickBlockKey(ItemKeys.MANGROVE_SIGN);
        BAMBOO_WALL_SIGN.itematic$setPickBlockKey(ItemKeys.BAMBOO_SIGN);
        OAK_WALL_HANGING_SIGN.itematic$setPickBlockKey(ItemKeys.OAK_HANGING_SIGN);
        SPRUCE_WALL_HANGING_SIGN.itematic$setPickBlockKey(ItemKeys.SPRUCE_HANGING_SIGN);
        BIRCH_WALL_HANGING_SIGN.itematic$setPickBlockKey(ItemKeys.BIRCH_HANGING_SIGN);
        ACACIA_WALL_HANGING_SIGN.itematic$setPickBlockKey(ItemKeys.ACACIA_HANGING_SIGN);
        CHERRY_WALL_HANGING_SIGN.itematic$setPickBlockKey(ItemKeys.CHERRY_HANGING_SIGN);
        JUNGLE_WALL_HANGING_SIGN.itematic$setPickBlockKey(ItemKeys.JUNGLE_HANGING_SIGN);
        DARK_OAK_WALL_HANGING_SIGN.itematic$setPickBlockKey(ItemKeys.DARK_OAK_HANGING_SIGN);
        MANGROVE_WALL_HANGING_SIGN.itematic$setPickBlockKey(ItemKeys.MANGROVE_HANGING_SIGN);
        CRIMSON_WALL_HANGING_SIGN.itematic$setPickBlockKey(ItemKeys.CRIMSON_HANGING_SIGN);
        WARPED_WALL_HANGING_SIGN.itematic$setPickBlockKey(ItemKeys.WARPED_HANGING_SIGN);
        BAMBOO_WALL_HANGING_SIGN.itematic$setPickBlockKey(ItemKeys.BAMBOO_HANGING_SIGN);
        REDSTONE_WALL_TORCH.itematic$setPickBlockKey(ItemKeys.REDSTONE_TORCH);
        SOUL_WALL_TORCH.itematic$setPickBlockKey(ItemKeys.SOUL_TORCH);
        WATER_CAULDRON.itematic$setPickBlockKey(ItemKeys.CAULDRON);
        LAVA_CAULDRON.itematic$setPickBlockKey(ItemKeys.CAULDRON);
        POWDER_SNOW_CAULDRON.itematic$setPickBlockKey(ItemKeys.CAULDRON);
        COCOA.itematic$setPickBlockKey(ItemKeys.COCOA_BEANS);
        TRIPWIRE.itematic$setPickBlockKey(ItemKeys.STRING);
        POTTED_TORCHFLOWER.itematic$setPickBlockKey(ItemKeys.TORCHFLOWER);
        POTTED_OAK_SAPLING.itematic$setPickBlockKey(ItemKeys.OAK_SAPLING);
        POTTED_SPRUCE_SAPLING.itematic$setPickBlockKey(ItemKeys.SPRUCE_SAPLING);
        POTTED_BIRCH_SAPLING.itematic$setPickBlockKey(ItemKeys.BIRCH_SAPLING);
        POTTED_JUNGLE_SAPLING.itematic$setPickBlockKey(ItemKeys.JUNGLE_SAPLING);
        POTTED_ACACIA_SAPLING.itematic$setPickBlockKey(ItemKeys.ACACIA_SAPLING);
        POTTED_CHERRY_SAPLING.itematic$setPickBlockKey(ItemKeys.CHERRY_SAPLING);
        POTTED_DARK_OAK_SAPLING.itematic$setPickBlockKey(ItemKeys.DARK_OAK_SAPLING);
        POTTED_MANGROVE_PROPAGULE.itematic$setPickBlockKey(ItemKeys.MANGROVE_PROPAGULE);
        POTTED_FERN.itematic$setPickBlockKey(ItemKeys.FERN);
        POTTED_DANDELION.itematic$setPickBlockKey(ItemKeys.DANDELION);
        POTTED_POPPY.itematic$setPickBlockKey(ItemKeys.POPPY);
        POTTED_BLUE_ORCHID.itematic$setPickBlockKey(ItemKeys.BLUE_ORCHID);
        POTTED_ALLIUM.itematic$setPickBlockKey(ItemKeys.ALLIUM);
        POTTED_AZURE_BLUET.itematic$setPickBlockKey(ItemKeys.AZURE_BLUET);
        POTTED_RED_TULIP.itematic$setPickBlockKey(ItemKeys.RED_TULIP);
        POTTED_ORANGE_TULIP.itematic$setPickBlockKey(ItemKeys.ORANGE_TULIP);
        POTTED_WHITE_TULIP.itematic$setPickBlockKey(ItemKeys.WHITE_TULIP);
        POTTED_PINK_TULIP.itematic$setPickBlockKey(ItemKeys.PINK_TULIP);
        POTTED_OXEYE_DAISY.itematic$setPickBlockKey(ItemKeys.OXEYE_DAISY);
        POTTED_CORNFLOWER.itematic$setPickBlockKey(ItemKeys.CORNFLOWER);
        POTTED_LILY_OF_THE_VALLEY.itematic$setPickBlockKey(ItemKeys.LILY_OF_THE_VALLEY);
        POTTED_WITHER_ROSE.itematic$setPickBlockKey(ItemKeys.WITHER_ROSE);
        POTTED_RED_MUSHROOM.itematic$setPickBlockKey(ItemKeys.RED_MUSHROOM);
        POTTED_BROWN_MUSHROOM.itematic$setPickBlockKey(ItemKeys.BROWN_MUSHROOM);
        POTTED_DEAD_BUSH.itematic$setPickBlockKey(ItemKeys.DEAD_BUSH);
        POTTED_CACTUS.itematic$setPickBlockKey(ItemKeys.CACTUS);
        SKELETON_WALL_SKULL.itematic$setPickBlockKey(ItemKeys.SKELETON_SKULL);
        WITHER_SKELETON_WALL_SKULL.itematic$setPickBlockKey(ItemKeys.WITHER_SKELETON_SKULL);
        ZOMBIE_WALL_HEAD.itematic$setPickBlockKey(ItemKeys.ZOMBIE_HEAD);
        PLAYER_WALL_HEAD.itematic$setPickBlockKey(ItemKeys.PLAYER_HEAD);
        CREEPER_WALL_HEAD.itematic$setPickBlockKey(ItemKeys.CREEPER_HEAD);
        DRAGON_WALL_HEAD.itematic$setPickBlockKey(ItemKeys.DRAGON_HEAD);
        PIGLIN_WALL_HEAD.itematic$setPickBlockKey(ItemKeys.PIGLIN_HEAD);
        WHITE_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.WHITE_BANNER);
        ORANGE_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.ORANGE_BANNER);
        MAGENTA_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.MAGENTA_BANNER);
        LIGHT_BLUE_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.LIGHT_BLUE_BANNER);
        YELLOW_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.YELLOW_BANNER);
        LIME_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.LIME_BANNER);
        PINK_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.PINK_BANNER);
        GRAY_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.GRAY_BANNER);
        LIGHT_GRAY_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.LIGHT_GRAY_BANNER);
        CYAN_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.CYAN_BANNER);
        PURPLE_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.PURPLE_BANNER);
        BLUE_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.BLUE_BANNER);
        BROWN_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.BROWN_BANNER);
        GREEN_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.GREEN_BANNER);
        RED_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.RED_BANNER);
        BLACK_WALL_BANNER.itematic$setPickBlockKey(ItemKeys.BLACK_BANNER);
        PITCHER_CROP.itematic$setPickBlockKey(ItemKeys.PITCHER_POD);
        DEAD_TUBE_CORAL_WALL_FAN.itematic$setPickBlockKey(ItemKeys.DEAD_TUBE_CORAL_FAN);
        DEAD_BRAIN_CORAL_WALL_FAN.itematic$setPickBlockKey(ItemKeys.DEAD_BRAIN_CORAL_FAN);
        DEAD_BUBBLE_CORAL_WALL_FAN.itematic$setPickBlockKey(ItemKeys.DEAD_BUBBLE_CORAL_FAN);
        DEAD_FIRE_CORAL_WALL_FAN.itematic$setPickBlockKey(ItemKeys.DEAD_FIRE_CORAL_FAN);
        DEAD_HORN_CORAL_WALL_FAN.itematic$setPickBlockKey(ItemKeys.DEAD_HORN_CORAL_FAN);
        TUBE_CORAL_WALL_FAN.itematic$setPickBlockKey(ItemKeys.TUBE_CORAL_FAN);
        BRAIN_CORAL_WALL_FAN.itematic$setPickBlockKey(ItemKeys.BRAIN_CORAL_FAN);
        BUBBLE_CORAL_WALL_FAN.itematic$setPickBlockKey(ItemKeys.BUBBLE_CORAL_FAN);
        FIRE_CORAL_WALL_FAN.itematic$setPickBlockKey(ItemKeys.FIRE_CORAL_FAN);
        HORN_CORAL_WALL_FAN.itematic$setPickBlockKey(ItemKeys.HORN_CORAL_FAN);
        POTTED_BAMBOO.itematic$setPickBlockKey(ItemKeys.BAMBOO);
        SWEET_BERRY_BUSH.itematic$setPickBlockKey(ItemKeys.SWEET_BERRIES);
        WEEPING_VINES_PLANT.itematic$setPickBlockKey(ItemKeys.WEEPING_VINES);
        TWISTING_VINES_PLANT.itematic$setPickBlockKey(ItemKeys.TWISTING_VINES);
        CRIMSON_WALL_SIGN.itematic$setPickBlockKey(ItemKeys.CRIMSON_SIGN);
        WARPED_WALL_SIGN.itematic$setPickBlockKey(ItemKeys.WARPED_SIGN);
        POTTED_CRIMSON_FUNGUS.itematic$setPickBlockKey(ItemKeys.CRIMSON_FUNGUS);
        POTTED_WARPED_FUNGUS.itematic$setPickBlockKey(ItemKeys.WARPED_FUNGUS);
        POTTED_CRIMSON_ROOTS.itematic$setPickBlockKey(ItemKeys.CRIMSON_ROOTS);
        POTTED_WARPED_ROOTS.itematic$setPickBlockKey(ItemKeys.WARPED_ROOTS);
        POWDER_SNOW.itematic$setPickBlockKey(ItemKeys.POWDER_SNOW_BUCKET);
        POTTED_AZALEA_BUSH.itematic$setPickBlockKey(ItemKeys.AZALEA);
        POTTED_FLOWERING_AZALEA_BUSH.itematic$setPickBlockKey(ItemKeys.FLOWERING_AZALEA);

        ((AbstractPlantStemBlockAccess) CAVE_VINES).itematic$setStemItemKey(ItemKeys.GLOW_BERRIES);
        ((AbstractPlantStemBlockAccess) KELP).itematic$setStemItemKey(ItemKeys.KELP);
        ((AbstractPlantStemBlockAccess) TWISTING_VINES).itematic$setStemItemKey(ItemKeys.TWISTING_VINES);
        ((AbstractPlantStemBlockAccess) WEEPING_VINES).itematic$setStemItemKey(ItemKeys.WEEPING_VINES);
    }
}
