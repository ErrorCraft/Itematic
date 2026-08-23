package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.references.BlockIds;
import net.errorcraft.itematic.tags.ItematicBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagsProvider<Block> {
    public BlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        this.builder(ItematicBlockTags.END_CRYSTAL_SPAWNABLE_ON)
            .add(BlockIds.BEDROCK)
            .add(BlockIds.OBSIDIAN);
        this.builder(ItematicBlockTags.TILLABLE_INTO_FARMLAND)
            .add(BlockIds.GRASS_BLOCK)
            .add(BlockIds.DIRT)
            .add(BlockIds.DIRT_PATH);
        this.builder(ItematicBlockTags.FLATTENABLE_INTO_DIRT_PATH)
            .add(BlockIds.GRASS_BLOCK)
            .add(BlockIds.DIRT)
            .add(BlockIds.PODZOL)
            .add(BlockIds.COARSE_DIRT)
            .add(BlockIds.MYCELIUM)
            .add(BlockIds.ROOTED_DIRT);
    }
}
