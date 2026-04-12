package net.errorcraft.itematic.serialization;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.registry.RegistryOps;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RegistryMapperCodec<J, A> implements Codec<A> {
    private final Codec<J> fromCodec;
    private final BiFunction<J, RegistryOps<?>, Optional<A>> to;
    private final Function<A, J> from;

    private RegistryMapperCodec(Codec<J> fromCodec, BiFunction<J, RegistryOps<?>, Optional<A>> to, Function<A, J> from) {
        this.fromCodec = fromCodec;
        this.to = to;
        this.from = from;
    }

    public static <J, A> RegistryMapperCodec<J, A> of(Codec<J> fromCodec, BiFunction<J, RegistryOps<?>, Optional<A>> to, Function<A, J> from) {
        return new RegistryMapperCodec<>(fromCodec, to, from);
    }

    @Override
    public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
        if (!(ops instanceof RegistryOps<T> registryOps)) {
            return DataResult.error(() -> "Registries are inaccessible");
        }

        return this.fromCodec.decode(ops, input)
            .map(Pair::getFirst)
            .map(joined -> this.to.apply(joined, registryOps))
            .flatMap(result -> result.map(DataResult::success)
                .orElseGet(() -> DataResult.error(() -> "Invalid name: " + input)))
            .map(result -> Pair.of(result, input));
    }

    @Override
    public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
        return this.fromCodec.encode(this.from.apply(input), ops, prefix);
    }
}
