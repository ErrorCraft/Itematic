package net.errorcraft.itematic.predicate;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.mixin.predicate.NumberRangeAccessor;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.NumberRange;

import java.util.Optional;

public class NumberRangeUtil {
    public static final PacketCodec<ByteBuf, NumberRange.IntRange> INTEGER_RANGE_PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.optional(PacketCodecs.VAR_INT), NumberRange.IntRange::min,
        PacketCodecs.optional(PacketCodecs.VAR_INT), NumberRange.IntRange::max,
        NumberRangeAccessor.IntRangeAccessor::create
    );

    private NumberRangeUtil() {}

    public record FloatRange(Optional<Float> min, Optional<Float> max) implements NumberRange<Float> {
        public static final Codec<FloatRange> CODEC = NumberRange.createCodec(Codec.FLOAT, FloatRange::new);

        public static FloatRange exactly(float value) {
            return new FloatRange(Optional.of(value), Optional.of(value));
        }

        public boolean test(float value) {
            if (this.min.isPresent() && this.min.get() > value) {
                return false;
            }

            return this.max.isEmpty() || value <= this.max.get();
        }
    }
}
