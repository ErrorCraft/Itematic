package net.errorcraft.itematic.predicate;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.mixin.advancements.criterion.MinMaxBoundsAccessor;
import net.minecraft.advancements.criterion.MinMaxBounds;

public class NumberRanges {
    private NumberRanges() {}

    public record FloatRange(Bounds<Float> bounds) implements MinMaxBounds<Float> {
        public static final Codec<FloatRange> CODEC = MinMaxBoundsAccessor.BoundsAccessor.createCodec(Codec.FLOAT)
            .xmap(FloatRange::new, FloatRange::bounds);

        public static FloatRange exactly(float value) {
            return new FloatRange(Bounds.exactly(value));
        }

        public boolean test(float value) {
            if (this.bounds.min().isPresent() && this.bounds.min().get() > value) {
                return false;
            }

            return this.bounds.max().isEmpty() || value <= this.bounds.max().get();
        }
    }
}
