package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.block.BlockKeys;
import net.errorcraft.itematic.block.ItematicBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider<Block> {
    public BlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        this.builder(ItematicBlockTags.END_CRYSTAL_SPAWNABLE_ON)
            .add(BlockKeys.BEDROCK)
            .add(BlockKeys.OBSIDIAN);
        this.builder(ItematicBlockTags.TILLABLE_INTO_FARMLAND)
            .add(BlockKeys.GRASS_BLOCK)
            .add(BlockKeys.DIRT)
            .add(BlockKeys.DIRT_PATH);
        this.builder(ItematicBlockTags.FLATTENABLE_INTO_DIRT_PATH)
            .add(BlockKeys.GRASS_BLOCK)
            .add(BlockKeys.DIRT)
            .add(BlockKeys.PODZOL)
            .add(BlockKeys.COARSE_DIRT)
            .add(BlockKeys.MYCELIUM)
            .add(BlockKeys.ROOTED_DIRT);
    }
}
