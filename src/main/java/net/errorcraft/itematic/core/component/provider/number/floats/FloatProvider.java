package net.errorcraft.itematic.core.component.provider.number.floats;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface FloatProvider<P extends FloatProvider<P, C>, C> {
    MapCodec<FloatProvider<?, ?>> MAP_CODEC = ItematicBuiltInRegistries.DATA_COMPONENT_CONTEXT_FLOAT_PROVIDER_TYPE.byNameCodec()
        .dispatchMap(
            "component",
            FloatProvider::type,
            Type::codec
        );

    Type<P, C> type();
    DataComponentType<C> component();
    float get(C component, DataComponentGetter components) throws ArithmeticException;
    static <T> T getComponentOrThrow(DataComponentGetter components, DataComponentType<T> component) {
        T value = components.get(component);
        if (value == null) {
            throw new ArithmeticException("Data component " + component + " is not present");
        }

        return value;
    }

    record Type<P extends FloatProvider<P, C>, C>(MapCodec<? extends P> codec) {
        public static <P extends FloatProvider<P, C>, C> Type<P, C> simple(MapCodec<? extends P> codec) {
            return new Type<>(codec);
        }

        public static <P extends FloatProvider<P, C>, C> Type<P, C> mapped(Function<P, MapCodec<? extends P>> subCodecSupplier, UnaryOperator<Type.Builder<P, C>> builder) {
            return builder.apply(new Type.Builder<>(subCodecSupplier)).build();
        }

        public static class Builder<P extends FloatProvider<P, C>, C> {
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

            public Type.Builder<P, C> put(String name, MapCodec<? extends P> codec) {
                this.mapper.put(name, codec);
                return this;
            }
        }
    }
}
