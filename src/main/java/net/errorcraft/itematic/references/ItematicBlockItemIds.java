package net.errorcraft.itematic.references;

import net.errorcraft.itematic.world.level.block.CoralCollection;
import net.errorcraft.itematic.world.level.block.CutoutCollection;
import net.errorcraft.itematic.world.level.block.WoodCollection;
import net.minecraft.references.BlockItemId;

import java.util.function.UnaryOperator;

public class ItematicBlockItemIds {
    public static final CutoutCollection<BlockItemId> STONE = createCutout("stone", CutoutCollection.Builder::noWall);
    public static final CutoutCollection<BlockItemId> COBBLESTONE = createCutout("cobblestone");
    public static final CutoutCollection<BlockItemId> MOSSY_COBBLESTONE = createCutout("mossy_cobblestone");
    public static final CutoutCollection<BlockItemId> SMOOTH_STONE = createCutout("smooth_stone", builder -> builder.noStairs().noWall());
    public static final CutoutCollection<BlockItemId> STONE_BRICKS = createCutout("stone_brick", builder -> builder.block("stone_bricks"));
    public static final CutoutCollection<BlockItemId> MOSSY_STONE_BRICKS = createCutout("mossy_stone_brick", builder -> builder.block("mossy_stone_bricks"));
    public static final CutoutCollection<BlockItemId> GRANITE = createCutout("granite");
    public static final CutoutCollection<BlockItemId> POLISHED_GRANITE = createCutout("polished_granite", CutoutCollection.Builder::noWall);
    public static final CutoutCollection<BlockItemId> DIORITE = createCutout("diorite");
    public static final CutoutCollection<BlockItemId> POLISHED_DIORITE = createCutout("polished_diorite", CutoutCollection.Builder::noWall);
    public static final CutoutCollection<BlockItemId> ANDESITE = createCutout("andesite");
    public static final CutoutCollection<BlockItemId> POLISHED_ANDESITE = createCutout("polished_andesite", CutoutCollection.Builder::noWall);
    public static final CutoutCollection<BlockItemId> COBBLED_DEEPSLATE = createCutout("cobbled_deepslate");
    public static final CutoutCollection<BlockItemId> POLISHED_DEEPSLATE = createCutout("polished_deepslate");
    public static final CutoutCollection<BlockItemId> DEEPSLATE_BRICKS = createCutout("deepslate_brick", builder -> builder.block("deepslate_bricks"));
    public static final CutoutCollection<BlockItemId> DEEPSLATE_TILES = createCutout("deepslate_tile", builder -> builder.block("deepslate_tiles"));
    public static final CutoutCollection<BlockItemId> TUFF = createCutout("tuff");
    public static final CutoutCollection<BlockItemId> POLISHED_TUFF = createCutout("polished_tuff");
    public static final CutoutCollection<BlockItemId> TUFF_BRICKS = createCutout("tuff_brick", builder -> builder.block("tuff_bricks"));
    public static final CutoutCollection<BlockItemId> BRICKS = createCutout("brick", builder -> builder.block("bricks"));
    public static final CutoutCollection<BlockItemId> MUD_BRICK = createCutout("mud_brick", builder -> builder.block("mud_bricks"));
    public static final CutoutCollection<BlockItemId> RESIN_BRICKS = createCutout("resin_brick", builder -> builder.block("resin_bricks"));
    public static final CutoutCollection<BlockItemId> SANDSTONE = createCutout("sandstone");
    public static final CutoutCollection<BlockItemId> SMOOTH_SANDSTONE = createCutout("smooth_sandstone", CutoutCollection.Builder::noWall);
    public static final CutoutCollection<BlockItemId> CUT_SANDSTONE = createCutout("cut_sandstone", builder -> builder.noStairs().noWall());
    public static final CutoutCollection<BlockItemId> RED_SANDSTONE = createCutout("red_sandstone");
    public static final CutoutCollection<BlockItemId> SMOOTH_RED_SANDSTONE = createCutout("smooth_red_sandstone", CutoutCollection.Builder::noWall);
    public static final CutoutCollection<BlockItemId> CUT_RED_SANDSTONE = createCutout("cut_red_sandstone", builder -> builder.noStairs().noWall());
    public static final CutoutCollection<BlockItemId> CINNABAR = createCutout("cinnabar");
    public static final CutoutCollection<BlockItemId> POLISHED_CINNABAR = createCutout("polished_cinnabar");
    public static final CutoutCollection<BlockItemId> CINNABAR_BRICKS = createCutout("cinnabar_brick", builder -> builder.block("cinnabar_bricks"));
    public static final CutoutCollection<BlockItemId> SULFUR = createCutout("sulfur");
    public static final CutoutCollection<BlockItemId> POLISHED_SULFUR = createCutout("polished_sulfur");
    public static final CutoutCollection<BlockItemId> SULFUR_BRICKS = createCutout("sulfur_brick", builder -> builder.block("sulfur_bricks"));
    public static final CutoutCollection<BlockItemId> PRISMARINE = createCutout("prismarine");
    public static final CutoutCollection<BlockItemId> PRISMARINE_BRICKS = createCutout("prismarine_brick", builder -> builder.block("prismarine_bricks").noWall());
    public static final CutoutCollection<BlockItemId> DARK_PRISMARINE = createCutout("dark_prismarine", CutoutCollection.Builder::noWall);
    public static final CutoutCollection<BlockItemId> NETHER_BRICKS = createCutout("nether_brick", builder -> builder.block("nether_bricks"));
    public static final CutoutCollection<BlockItemId> RED_NETHER_BRICKS = createCutout("red_nether_brick", builder -> builder.block("red_nether_bricks"));
    public static final CutoutCollection<BlockItemId> BLACKSTONE = createCutout("blackstone");
    public static final CutoutCollection<BlockItemId> POLISHED_BLACKSTONE = createCutout("polished_blackstone");
    public static final CutoutCollection<BlockItemId> POLISHED_BLACKSTONE_BRICKS = createCutout("polished_blackstone_brick", builder -> builder.block("polished_blackstone_bricks"));
    public static final CutoutCollection<BlockItemId> END_STONE_BRICKS = createCutout("end_stone_brick", builder -> builder.block("end_stone_bricks"));
    public static final CutoutCollection<BlockItemId> PURPUR = createCutout("purpur", builder -> builder.block("purpur_block").noWall());
    public static final CutoutCollection<BlockItemId> QUARTZ = createCutout("quartz", builder -> builder.block("quartz_block").noWall());
    public static final CutoutCollection<BlockItemId> SMOOTH_QUARTZ = createCutout("smooth_quartz", CutoutCollection.Builder::noWall);
    public static final WoodCollection<BlockItemId> OAK = createWood("oak");
    public static final WoodCollection<BlockItemId> SPRUCE = createWood("spruce");
    public static final WoodCollection<BlockItemId> BIRCH = createWood("birch");
    public static final WoodCollection<BlockItemId> JUNGLE = createWood("jungle");
    public static final WoodCollection<BlockItemId> ACACIA = createWood("acacia");
    public static final WoodCollection<BlockItemId> CHERRY = createWood("cherry");
    public static final WoodCollection<BlockItemId> DARK_OAK = createWood("dark_oak");
    public static final WoodCollection<BlockItemId> PALE_OAK = createWood("pale_oak");
    public static final WoodCollection<BlockItemId> MANGROVE = createWood("mangrove", WoodCollection.SUFFIXES_MANGROVE);
    public static final WoodCollection<BlockItemId> BAMBOO = createWood("bamboo", WoodCollection.SUFFIXES_BAMBOO);
    public static final WoodCollection<BlockItemId> CRIMSON = createNetherWood("crimson");
    public static final WoodCollection<BlockItemId> WARPED = createNetherWood("warped");
    public static final CutoutCollection<BlockItemId> BAMBOO_MOSAIC = createCutout("bamboo_mosaic", CutoutCollection.Builder::noWall);
    public static final CoralCollection<BlockItemId> TUBE_CORAL = createCoral("tube");
    public static final CoralCollection<BlockItemId> BRAIN_CORAL = createCoral("brain");
    public static final CoralCollection<BlockItemId> BUBBLE_CORAL = createCoral("bubble");
    public static final CoralCollection<BlockItemId> FIRE_CORAL = createCoral("fire");
    public static final CoralCollection<BlockItemId> HORN_CORAL = createCoral("horn");

    private ItematicBlockItemIds() {}

    private static CutoutCollection<BlockItemId> createCutout(String baseName) {
        return CutoutCollection.builder(baseName)
            .build()
            .map(BlockItemId::create);
    }

    private static CutoutCollection<BlockItemId> createCutout(String baseName, UnaryOperator<CutoutCollection.Builder> builder) {
        return builder.apply(CutoutCollection.builder(baseName))
            .build()
            .map(BlockItemId::create);
    }

    private static WoodCollection<BlockItemId> createWood(String name) {
        return createWood(name, WoodCollection.SUFFIXES);
    }

    private static WoodCollection<BlockItemId> createNetherWood(String name) {
        return createWood(name, WoodCollection.SUFFIXES_NETHER);
    }

    private static WoodCollection<BlockItemId> createWood(String name, WoodCollection<String> suffixes) {
        return WoodCollection.affixWithType(WoodCollection.create(name), suffixes)
            .map(BlockItemId::create);
    }

    private static CoralCollection<BlockItemId> createCoral(String name) {
        return CoralCollection.affixWithType(CoralCollection.create(name))
            .map(BlockItemId::create);
    }
}
