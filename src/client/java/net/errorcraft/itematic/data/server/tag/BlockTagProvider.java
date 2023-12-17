package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.block.BlockKeys;
import net.errorcraft.itematic.block.ItematicBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider<Block> {
    public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BLOCK, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(ItematicBlockTags.END_CRYSTAL_SPAWNABLE_ON)
            .add(BlockKeys.BEDROCK)
            .add(BlockKeys.OBSIDIAN);
        this.getOrCreateTagBuilder(ItematicBlockTags.SWORD_SUPER_EFFICIENT)
            .add(BlockKeys.COBWEB);
        this.getOrCreateTagBuilder(ItematicBlockTags.SHEARS_EFFICIENT)
            .add(BlockKeys.VINE)
            .add(BlockKeys.GLOW_LICHEN);
        this.getOrCreateTagBuilder(ItematicBlockTags.SHEARS_SLIGHTLY_EFFICIENT)
            .forceAddTag(BlockTags.WOOL);
        this.getOrCreateTagBuilder(ItematicBlockTags.SHEARS_SUPER_EFFICIENT)
            .add(BlockKeys.COBWEB)
            .forceAddTag(BlockTags.LEAVES);
        this.getOrCreateTagBuilder(ItematicBlockTags.TILLABLE_INTO_FARMLAND)
            .add(BlockKeys.GRASS_BLOCK)
            .add(BlockKeys.DIRT)
            .add(BlockKeys.DIRT_PATH);
        this.getOrCreateTagBuilder(ItematicBlockTags.AIR)
            .add(BlockKeys.AIR)
            .add(BlockKeys.CAVE_AIR)
            .add(BlockKeys.VOID_AIR);
        this.getOrCreateTagBuilder(ItematicBlockTags.FLATTENABLE_INTO_DIRT_PATH)
            .add(BlockKeys.GRASS_BLOCK)
            .add(BlockKeys.DIRT)
            .add(BlockKeys.PODZOL)
            .add(BlockKeys.COARSE_DIRT)
            .add(BlockKeys.MYCELIUM)
            .add(BlockKeys.ROOTED_DIRT);
    }
}
