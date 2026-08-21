package net.errorcraft.itematic.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class RandomRange<T extends Comparable<T>> {
    protected final T min;
    protected final T max;

    protected RandomRange(T min, T max) {
        if (max.compareTo(min) < 0) {
            throw new IllegalArgumentException("The maximum (" + max + ") must be at least the minimum (" + min + ")");
        }

        this.min = min;
        this.max = max;
    }

    public abstract T get(RandomSource random);

    public T min() {
        return this.min;
    }

    public T max() {
        return this.max;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof RandomRange<?> that) {
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

    private static <T extends Comparable<T>, S extends RandomRange<T>> Codec<S> codec(Codec<T> codec, T min, T max, BiFunction<T, T, S> creator, Function<T, S> singleValueCreator) {
        Codec<S> elementCodec = RecordCodecBuilder.create(instance -> instance.group(
            codec.optionalFieldOf("min", min).forGetter(RandomRange::min),
            codec.optionalFieldOf("max", max).forGetter(RandomRange::max)
        ).apply(instance, creator));
        return Codec.either(codec, elementCodec)
            .xmap(
                either -> either.map(singleValueCreator, Function.identity()),
                range -> {
                    if (range.min.compareTo(range.max) == 0) {
                        return Either.left(range.min);
                    }

                    return Either.right(range);
                }
            ).validate(RandomRange::validate);
    }

    private static <T extends Comparable<T>, S extends RandomRange<T>> DataResult<S> validate(S range) {
        if (range.max.compareTo(range.min) < 0) {
            return DataResult.error(() -> "Max must be at least min: " + range);
        }

        return DataResult.success(range);
    }

    public static class Integers extends RandomRange<Integer> {
        public static final Codec<Integers> CODEC = codec(
            Codec.INT,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            Integers::new,
            Integers::exactly
        );

        private Integers(int min, int max) {
            super(min, max);
        }

        public static Integers of(int min, int max) {
            return new Integers(min, max);
        }

        public static Integers exactly(int value) {
            return new Integers(value, value);
        }

        public static Integers atLeast(int min) {
            return new Integers(min, Integer.MAX_VALUE);
        }

        @Override
        public Integer get(RandomSource random) {
            if (this.min.equals(this.max)) {
                return this.min;
            }

            return random.nextIntBetweenInclusive(this.min, this.max);
        }
    }

    public static class Floats extends RandomRange<Float> {
        public static final Codec<Floats> CODEC = codec(
            Codec.FLOAT,
            -Float.MAX_VALUE,
            Float.MAX_VALUE,
            Floats::new,
            Floats::exactly
        );

        private Floats(float min, float max) {
            super(min, max);
        }

        public static Floats of(float min, float max) {
            return new Floats(min, max);
        }

        public static Floats exactly(float value) {
            return new Floats(value, value);
        }

        @Override
        public Float get(RandomSource random) {
            if (this.min.equals(this.max)) {
                return this.min;
            }

            return random.nextFloat() * (this.max - this.min) + this.min;
        }
    }

    public static class Doubles extends RandomRange<Double> {
        public static final Codec<Doubles> CODEC = codec(
            Codec.DOUBLE,
            -Double.MAX_VALUE,
            Double.MAX_VALUE,
            Doubles::new,
            Doubles::exactly
        );

        private Doubles(double min, double max) {
            super(min, max);
        }

        public static Doubles of(double min, double max) {
            return new Doubles(min, max);
        }

        public static Doubles exactly(double value) {
            return new Doubles(value, value);
        }

        @Override
        public Double get(RandomSource random) {
            if (this.min.equals(this.max)) {
                return this.min;
            }

            return random.nextDouble() * (this.max - this.min) + this.min;
        }
    }
}
