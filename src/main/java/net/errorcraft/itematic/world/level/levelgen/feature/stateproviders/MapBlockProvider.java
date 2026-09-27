package net.errorcraft.itematic.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MapBlockProvider extends BlockStateProvider {
    public static final Codec<Block> BLOCK_CODEC = BuiltInRegistries.BLOCK.byNameCodec();
    public static final MapCodec<MapBlockProvider> CODEC = Codec.unboundedMap(BLOCK_CODEC, BLOCK_CODEC).fieldOf("blocks")
        .xmap(
            MapBlockProvider::new,
            provider -> provider.blocks
        );

    private final Map<Block, Block> blocks;

    public MapBlockProvider(Map<Block, Block> blocks) {
        this.blocks = blocks;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return ItematicBlockStateProviderTypes.MAP_BLOCK;
    }

    @Override
    public BlockState getState(LevelAccessor level, RandomSource random, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Block newBlock = this.blocks.get(state.getBlock());
        if (newBlock == null) {
            return state;
        }

        return newBlock.defaultBlockState().withPropertiesOf(state);
    }

    @Override
    public @Nullable BlockState getOptionalState(LevelAccessor level, RandomSource random, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Block newBlock = this.blocks.get(state.getBlock());
        if (newBlock == null) {
            return null;
        }

        return newBlock.defaultBlockState().withPropertiesOf(state);
    }

    public static class Builder {
        private final Map<Block, Block> blocks = new HashMap<>();

        public MapBlockProvider build() {
            return new MapBlockProvider(this.blocks);
        }

        public Builder add(Block from, Block to) {
            this.blocks.put(from, to);
            return this;
        }
    }
}
