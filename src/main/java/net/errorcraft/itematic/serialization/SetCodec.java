package net.errorcraft.itematic.serialization;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.registry.Registry;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SetCodec<E> implements Codec<Set<E>> {
    private final Codec<List<E>> listCodec;
    private final Comparator<E> comparator;

    private SetCodec(Codec<E> codec, Comparator<E> comparator) {
        this.listCodec = codec.listOf();
        this.comparator = comparator;
    }

    @SuppressWarnings("DataFlowIssue")
    public static <T> Codec<Set<T>> forRegistry(Registry<T> registry) {
        return new SetCodec<>(registry.getCodec(), Comparator.comparing(registry::getId));
    }

    public static <T extends Enum<T>> Codec<Set<T>> forEnum(Codec<T> codec) {
        return new SetCodec<>(codec, Enum::compareTo);
    }

    @Override
    public <T> DataResult<Pair<Set<E>, T>> decode(DynamicOps<T> ops, T input) {
        return this.listCodec.decode(ops, input)
            .flatMap(pair -> {
                List<E> elements = pair.getFirst();
                Set<E> set = new HashSet<>();
                Set<E> duplicates = new HashSet<>();
                for (E element : elements) {
                    if (!set.add(element)) {
                        duplicates.add(element);
                    }
                }

                if (!duplicates.isEmpty()) {
                    return DataResult.error(() -> "Set contained duplicate entries: " + duplicates.stream().map(E::toString).collect(Collectors.joining(", ")));
                }

                return DataResult.success(Pair.of(set, pair.getSecond()));
            });
    }

    @Override
    public <T> DataResult<T> encode(Set<E> input, DynamicOps<T> ops, T prefix) {
        return this.listCodec.encode(input.stream().sorted(this.comparator).toList(), ops, prefix);
    }
}
