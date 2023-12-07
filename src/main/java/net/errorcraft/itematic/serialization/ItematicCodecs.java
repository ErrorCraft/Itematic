package net.errorcraft.itematic.serialization;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ItematicCodecs {
    private ItematicCodecs() {}

    public static <T, S extends T> Codec<T> alternatively(Codec<T> elementCodec, Codec<S> alternativeCodec, Function<T, @Nullable S> elementToAlternativeMapper) {
        return Codecs.either(elementCodec, alternativeCodec)
            .xmap(either -> either.map(t -> t, s -> s), t -> {
                S s = elementToAlternativeMapper.apply(t);
                return s == null ? Either.left(t) : Either.right(s);
            });
    }

    public static <T> Codec<T> alternatively(Codec<T> elementCodec, Codec<T> alternativeCodec, Predicate<T> elementPredicate) {
        return Codecs.either(elementCodec, alternativeCodec)
            .xmap(either -> either.map(t -> t, t -> t), t -> elementPredicate.test(t) ? Either.left(t) : Either.right(t));
    }

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
}
