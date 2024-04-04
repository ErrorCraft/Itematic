package net.errorcraft.itematic.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Range<T extends Comparable<T>> {
    public static final Codec<Range.IntegerRange> INT_CODEC = Codec.lazyInitialized(() -> createCodec(Codec.INT, Integer.MIN_VALUE, Integer.MAX_VALUE, IntegerRange::new, IntegerRange::of));
    public static final Codec<Range.FloatRange> FLOAT_CODEC = Codec.lazyInitialized(() -> createCodec(Codec.FLOAT, -Float.MAX_VALUE, Float.MAX_VALUE, FloatRange::new, FloatRange::of));
    public static final Codec<Range.DoubleRange> DOUBLE_CODEC = Codec.lazyInitialized(() -> createCodec(Codec.DOUBLE, -Double.MAX_VALUE, Double.MAX_VALUE, DoubleRange::new, DoubleRange::of));

    protected final T min;
    protected final T max;

    protected Range(T min, T max) {
        if (max.compareTo(min) < 0) {
            throw new IllegalArgumentException("The maximum (" + max + ") must be at least the minimum (" + min + ")");
        }
        this.min = min;
        this.max = max;
    }

    @NotNull
    public abstract T get(Random random);

    @NotNull
    public T min() {
        return this.min;
    }

    @NotNull
    public T max() {
        return this.max;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Range<?> that) {
            return Objects.equals(this.min, that.min)
                && Objects.equals(this.max, that.max);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.min, this.max);
    }

    @Override
    public String toString() {
        return "Range[" +
            "min=" + min + ", " +
            "max=" + max + ']';
    }

    private static <T extends Comparable<T>, S extends Range<T>> Codec<S> createCodec(Codec<T> codec, T min, T max, BiFunction<T, T, S> creator, Function<T, S> singleValueCreator) {
        Codec<S> elementCodec = RecordCodecBuilder.create(instance -> instance.group(
            codec.optionalFieldOf("min", min).forGetter(Range::min),
            codec.optionalFieldOf("max", max).forGetter(Range::max)
        ).apply(instance, creator));
        return Codec.either(codec, elementCodec).xmap(either -> either.map(singleValueCreator, s -> s), s -> {
            if (s.min.compareTo(s.max) == 0) {
                return Either.left(s.min);
            }
            return Either.right(s);
        }).validate(Range::validate);
    }

    private static <T extends Comparable<T>, S extends Range<T>> DataResult<S> validate(S s) {
        if (s.max.compareTo(s.min) < 0) {
            return DataResult.error(() -> "Max must be at least min: " + s);
        }
        return DataResult.success(s);
    }

    public static class IntegerRange extends Range<Integer> {
        private IntegerRange(int min, int max) {
            super(min, max);
        }

        @Override
        public @NotNull Integer get(Random random) {
            if (this.min.equals(this.max)) {
                return this.min;
            }
            return random.nextInt(this.max - this.min + 1) + this.min;
        }

        public static IntegerRange of(int min, int max) {
            return new IntegerRange(min, max);
        }

        public static IntegerRange of(int value) {
            return new IntegerRange(value, value);
        }

        public static IntegerRange atLeast(int min) {
            return new IntegerRange(min, Integer.MAX_VALUE);
        }
    }

    public static class FloatRange extends Range<Float> {
        private FloatRange(float min, float max) {
            super(min, max);
        }

        @Override
        public @NotNull Float get(Random random) {
            if (this.min.equals(this.max)) {
                return this.min;
            }
            return random.nextFloat() * (this.max - this.min) + this.min;
        }

        public static FloatRange of(float min, float max) {
            return new FloatRange(min, max);
        }

        public static FloatRange of(float value) {
            return new FloatRange(value, value);
        }
    }

    public static class DoubleRange extends Range<Double> {
        private DoubleRange(double min, double max) {
            super(min, max);
        }

        @Override
        public @NotNull Double get(Random random) {
            if (this.min.equals(this.max)) {
                return this.min;
            }
            return random.nextDouble() * (this.max - this.min) + this.min;
        }

        public static DoubleRange of(double min, double max) {
            return new DoubleRange(min, max);
        }

        public static DoubleRange of(double value) {
            return new DoubleRange(value, value);
        }
    }
}
