package net.errorcraft.itematic.serialization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.*;
import net.minecraft.registry.Registry;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class SetMapCodec<K, V> extends MapCodec<Set<V>> {
    private final Codec<K> keyCodec;
    private final Function<K, ? extends Decoder<? extends V>> decoder;
    private final Function<V, ? extends Encoder<V>> encoder;
    private final Function<V, K> type;
    private final Keyable keys;

    public SetMapCodec(Codec<K> keyCodec, Function<K, Decoder<? extends V>> decoder, Function<V, ? extends Encoder<V>> encoder, Function<V, K> type, Keyable keys) {
        this.keyCodec = keyCodec;
        this.decoder = decoder;
        this.encoder = encoder;
        this.type = type;
        this.keys = keys;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> SetMapCodec<K, V> ofRegistry(Registry<K> registry, Function<K, Codec<? extends V>> keyToCodec, Function<V, Codec<? extends V>> entryToCodec, Function<V, K> valueToKey) {
        return new SetMapCodec<>(
            registry.getCodec(),
            keyToCodec::apply,
            v -> (Encoder<V>) entryToCodec.apply(v),
            valueToKey,
            registry
        );
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return this.keys.keys(ops);
    }

    @Override
    public <T> DataResult<Set<V>> decode(DynamicOps<T> ops, MapLike<T> input) {
        final ImmutableSet.Builder<V> read = ImmutableSet.builder();
        final ImmutableList.Builder<Pair<T, T>> failed = ImmutableList.builder();

        final DataResult<Unit> result = input.entries().reduce(
            DataResult.success(Unit.INSTANCE, Lifecycle.stable()),
            (r, pair) -> {
                DataResult<V> valueResult = this.keyCodec.decode(ops, pair.getFirst()).flatMap(type -> {
                    Decoder<? extends V> entryDecoder = this.decoder.apply(type.getFirst());
                    return entryDecoder.decode(ops, pair.getSecond()).map(Pair::getFirst);
                });
                valueResult.error().ifPresent(e -> failed.add(pair));
                return r.apply2stable(
                    (u, v) -> {
                        read.add(v);
                        return u;
                    },
                    valueResult
                );
            },
            (r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2)
        );

        final Set<V> elements = read.build();
        final T errors = ops.createMap(failed.build().stream());

        return result.map(unit -> elements).setPartial(elements).mapError(e -> e + " missed input: " + errors);
    }

    @Override
    public <T> RecordBuilder<T> encode(Set<V> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
        for (V value : input) {
            final Encoder<V> elementEncoder = this.encoder.apply(value);
            final DataResult<T> result = elementEncoder.encodeStart(ops, value);
            K key = this.type.apply(value);
            final DataResult<T> resultKey = this.keyCodec.encodeStart(ops, key).setLifecycle(Lifecycle.stable());
            prefix.add(resultKey, result);
        }
        return prefix;
    }
}
