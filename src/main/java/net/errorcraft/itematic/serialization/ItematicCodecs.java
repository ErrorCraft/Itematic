package net.errorcraft.itematic.serialization;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ItematicCodecs {
    private ItematicCodecs() {}

    public static <T> Codec<List<T>> countRangeList(Codec<List<T>> codec, int minCount, int maxCount) {
        return Codecs.validate(codec, list -> {
            if (list.size() < minCount) {
                return DataResult.error(() -> "List must contain at least " + minCount + " elements");
            }
            if (list.size() > maxCount) {
                return DataResult.error(() -> "List must contain at most " + maxCount + " elements");
            }
            return DataResult.success(list);
        });
    }

    public static <T> Codec<Set<T>> setCodec(Codec<T> codec) {
        return new SetCodec<>(codec);
    }

    private static class SetCodec<E> implements Codec<Set<E>> {
        private final Codec<List<E>> listCodec;

        private SetCodec(Codec<E> codec) {
            this.listCodec = codec.listOf();
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
            return this.listCodec.encode(new ArrayList<>(input), ops, prefix);
        }
    }
}
