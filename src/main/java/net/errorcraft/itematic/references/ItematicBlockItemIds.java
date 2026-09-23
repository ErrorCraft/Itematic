package net.errorcraft.itematic.references;

import net.errorcraft.itematic.world.level.block.CoralCollection;
import net.errorcraft.itematic.world.level.block.CutoutCollection;
import net.errorcraft.itematic.world.level.block.WoodCollection;
import net.minecraft.references.BlockItemId;

public class ItematicBlockItemIds {
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
    public static final CutoutCollection<BlockItemId> BAMBOO_MOSAIC = createSimpleCutoutWithoutWall("bamboo_mosaic");
    public static final CoralCollection<BlockItemId> TUBE_CORAL = createCoral("tube");
    public static final CoralCollection<BlockItemId> BRAIN_CORAL = createCoral("brain");
    public static final CoralCollection<BlockItemId> BUBBLE_CORAL = createCoral("bubble");
    public static final CoralCollection<BlockItemId> FIRE_CORAL = createCoral("fire");
    public static final CoralCollection<BlockItemId> HORN_CORAL = createCoral("horn");
    public static final CutoutCollection<BlockItemId> CINNABAR = createSimpleCutout("cinnabar");
    public static final CutoutCollection<BlockItemId> POLISHED_CINNABAR = createSimpleCutout("polished_cinnabar");
    public static final CutoutCollection<BlockItemId> CINNABAR_BRICKS = createSimpleCutout("cinnabar_brick", "cinnabar_bricks");
    public static final CutoutCollection<BlockItemId> SULFUR = createSimpleCutout("sulfur");
    public static final CutoutCollection<BlockItemId> POLISHED_SULFUR = createSimpleCutout("polished_sulfur");
    public static final CutoutCollection<BlockItemId> SULFUR_BRICKS = createSimpleCutout("sulfur_brick", "sulfur_bricks");

    private ItematicBlockItemIds() {}

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

    private static CutoutCollection<BlockItemId> createSimpleCutout(String baseName) {
        return CutoutCollection.suffixWithTypeWall(CutoutCollection.create(baseName))
            .map(BlockItemId::create);
    }

    private static CutoutCollection<BlockItemId> createSimpleCutoutWithoutWall(String baseName) {
        return CutoutCollection.suffixWithTypeWall(CutoutCollection.createWithoutWall(baseName))
            .map(BlockItemId::create);
    }

    private static CutoutCollection<BlockItemId> createSimpleCutout(String baseName, String blockName) {
        return CutoutCollection.suffixWithTypeWall(CutoutCollection.create(baseName, blockName))
            .map(BlockItemId::create);
    }
}
