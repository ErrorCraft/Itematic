package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.tags.ItematicBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemIds;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagsProvider<Block> {
    public BlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        this.builder(ItematicBlockTags.END_CRYSTAL_SPAWNABLE_ON)
            .add(BlockItemIds.BEDROCK.block())
            .add(BlockItemIds.OBSIDIAN.block());
        this.builder(ItematicBlockTags.TILLABLE_INTO_FARMLAND)
            .add(BlockItemIds.GRASS_BLOCK.block())
            .add(BlockItemIds.DIRT.block())
            .add(BlockItemIds.DIRT_PATH.block());
        this.builder(ItematicBlockTags.FLATTENABLE_INTO_DIRT_PATH)
            .add(BlockItemIds.GRASS_BLOCK.block())
            .add(BlockItemIds.DIRT.block())
            .add(BlockItemIds.PODZOL.block())
            .add(BlockItemIds.COARSE_DIRT.block())
            .add(BlockItemIds.MYCELIUM.block())
            .add(BlockItemIds.ROOTED_DIRT.block());
        this.builder(ItematicBlockTags.HAS_MARKER_PARTICLE)
            .add(BlockItemIds.BARRIER.block())
            .add(BlockItemIds.LIGHT.block());
    }
}
