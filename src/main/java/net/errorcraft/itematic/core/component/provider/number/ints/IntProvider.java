package net.errorcraft.itematic.core.component.provider.number.ints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface IntProvider<P extends IntProvider<P, C>, C> {
    MapCodec<IntProvider<?, ?>> MAP_CODEC = ItematicBuiltInRegistries.DATA_COMPONENT_CONTEXT_INT_PROVIDER_TYPE.byNameCodec()
        .dispatchMap(
            "component",
            IntProvider::type,
            Type::codec
        );

    Type<P, C> type();
    DataComponentType<C> component();
    int get(C component, DataComponentGetter components);

    record Type<P extends IntProvider<P, C>, C>(MapCodec<? extends P> codec) {
        public static <P extends IntProvider<P, C>, C> Type<P, C> simple(MapCodec<? extends P> codec) {
            return new Type<>(codec);
        }

        public static <P extends IntProvider<P, C>, C> Type<P, C> mapped(Function<P, MapCodec<? extends P>> subCodecSupplier, UnaryOperator<IntProvider.Type.Builder<P, C>> builder) {
            return builder.apply(new Builder<>(subCodecSupplier)).build();
        }

        public static class Builder<P extends IntProvider<P, C>, C> {
            private final ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends P>> mapper = new ExtraCodecs.LateBoundIdMapper<>();
            private final Function<P, MapCodec<? extends P>> subCodecSupplier;

            private Builder(Function<P, MapCodec<? extends P>> subCodecSupplier) {
                this.subCodecSupplier = subCodecSupplier;
            }

            private Type<P, C> build() {
                MapCodec<? extends P> codec = this.mapper.codec(Codec.STRING).dispatchMap(
                    "provider",
                    this.subCodecSupplier,
                    Function.identity()
                );
                return new Type<>(codec);
            }

            public Builder<P, C> put(String name, MapCodec<? extends P> codec) {
                this.mapper.put(name, codec);
                return this;
            }
        }
    }
}
