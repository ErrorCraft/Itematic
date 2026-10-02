package net.errorcraft.itematic.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class ApplyPropertiesProvider implements BlockStateProvider {
    public static final MapCodec<ApplyPropertiesProvider> CODEC = BlockItemStateProperties.CODEC.fieldOf("properties")
        .xmap(
            ApplyPropertiesProvider::new,
            property -> property.properties
        );

    private final BlockItemStateProperties properties;

    public ApplyPropertiesProvider(BlockItemStateProperties properties) {
        this.properties = properties;
    }

    @Override
    public MapCodec<? extends BlockStateProvider> codec() {
        return CODEC;
    }

    @Override
    public BlockState getState(LevelAccessor level, RandomSource random, BlockPos pos) {
        return this.properties.apply(level.getBlockState(pos));
    }
}
