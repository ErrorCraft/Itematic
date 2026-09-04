package net.errorcraft.itematic.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;

public class ApplyPropertiesProvider extends BlockStateProvider {
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
    protected BlockStateProviderType<?> type() {
        return ItematicBlockStateProviderTypes.APPLY_PROPERTIES;
    }

    @Override
    public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
        return this.properties.apply(level.getBlockState(pos));
    }
}
