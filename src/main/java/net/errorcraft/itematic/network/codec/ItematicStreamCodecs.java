package net.errorcraft.itematic.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.apache.commons.lang3.math.Fraction;

import java.util.HashSet;
import java.util.Set;

public class ItematicStreamCodecs {
    public static final StreamCodec<ByteBuf, Fraction> FRACTION = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, Fraction::getNumerator,
        ByteBufCodecs.VAR_INT, Fraction::getDenominator,
        Fraction::getFraction
    );

    private ItematicStreamCodecs() {}

    public static <B extends ByteBuf, T> StreamCodec<B, Set<T>> set(StreamCodec<B, T> elementCodec) {
        return ByteBufCodecs.collection(HashSet::new, elementCodec);
    }
}
