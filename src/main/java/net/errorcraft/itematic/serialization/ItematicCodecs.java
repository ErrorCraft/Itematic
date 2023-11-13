package net.errorcraft.itematic.serialization;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ItematicCodecs {
    private ItematicCodecs() {}
    public static <T, S extends T> Codec<T> alternatively(Codec<T> elementCodec, Codec<S> alternativeCodec, Function<T, @Nullable S> elementToAlternativeMapper) {
        return Codecs.either(elementCodec, alternativeCodec)
            .xmap(either -> either.map(t -> t, s -> s), t -> {
                S s = elementToAlternativeMapper.apply(t);
                return s == null ? Either.left(t) : Either.right(s);
            });
    }
}
