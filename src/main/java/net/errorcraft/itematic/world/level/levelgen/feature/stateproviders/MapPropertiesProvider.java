package net.errorcraft.itematic.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class MapPropertiesProvider extends BlockStateProvider {
    public static final MapCodec<MapPropertiesProvider> CODEC = Properties.CODEC.fieldOf("properties")
        .xmap(
            MapPropertiesProvider::new,
            property -> property.properties
        );

    private final Properties properties;

    private MapPropertiesProvider(Properties properties) {
        this.properties = properties;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return ItematicBlockStateProviderTypes.MAP_PROPERTIES;
    }

    @Override
    public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        StateDefinition<Block, BlockState> stateDefinition = state.getBlock().getStateDefinition();
        for (Map.Entry<String, Properties.Entry> entry : this.properties.properties.entrySet()) {
            Property<?> property = stateDefinition.getProperty(entry.getKey());
            if (property != null) {
                state = updateState(state, property, entry.getValue());
            }
        }

        return state;
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState state, Property<T> property, Properties.Entry entry) {
        String currentValueName = property.getName(state.getValue(property));
        String newValueName = entry.map.get(currentValueName);
        if (newValueName == null) {
            return state;
        }

        return property.getValue(newValueName)
            .map(newValue -> state.setValue(property, newValue))
            .orElse(state);
    }

    private record Properties(Map<String, Entry> properties) {
        public static final Codec<Properties> CODEC = Codec.unboundedMap(Codec.STRING, Entry.CODEC)
            .xmap(Properties::new, Properties::properties);

        private record Entry(Map<String, String> map) {
            public static final Codec<Entry> CODEC = Codec.unboundedMap(Codec.STRING, Codec.STRING)
                .xmap(Entry::new, Entry::map);
        }
    }

    public static class Builder {
        private final Map<String, Properties.Entry> properties = new HashMap<>();

        private Builder() {}

        public MapPropertiesProvider build() {
            return new MapPropertiesProvider(new Properties(this.properties));
        }

        public <T extends Comparable<T>> Builder add(Property<T> property, UnaryOperator<EntryBuilder<T>> map) {
            this.properties.put(
                property.getName(),
                map.apply(new EntryBuilder<>(property)).build()
            );
            return this;
        }

        public static class EntryBuilder<T extends Comparable<T>> {
            private final Property<T> property;
            private final Map<String, String> map = new HashMap<>();

            private EntryBuilder(Property<T> property) {
                this.property = property;
            }

            private Properties.Entry build() {
                return new Properties.Entry(this.map);
            }

            public EntryBuilder<T> map(T from, T to) {
                this.map.put(
                    this.property.getName(from),
                    this.property.getName(to)
                );
                return this;
            }
        }
    }
}
