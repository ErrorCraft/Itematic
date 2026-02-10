package net.errorcraft.itematic.serialization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.util.dynamic.CodecsAccessor;
import net.minecraft.util.dynamic.Codecs;
import org.apache.commons.lang3.math.Fraction;

public class ItematicCodecs {
    public static final Codec<Float> NON_NEGATIVE_FLOAT = Codec.FLOAT.validate(value -> {
        if (value >= 0 && value <= Float.MAX_VALUE) {
            return DataResult.success(value);
        }
        return DataResult.error(() -> "Value must be non-negative: " + value);
    });
    public static final Codec<Double> NON_NEGATIVE_DOUBLE = Codec.DOUBLE.validate(value -> {
        if (value >= 0 && value <= Double.MAX_VALUE) {
            return DataResult.success(value);
        }
        return DataResult.error(() -> "Value must be non-negative: " + value);
    });
    public static final Codec<Integer> HUE = Codec.intRange(0, 360);
    public static final Codec<Fraction> POSITIVE_FRACTION = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("numerator").forGetter(Fraction::getNumerator),
        Codecs.POSITIVE_INT.fieldOf("denominator").forGetter(Fraction::getDenominator)
    ).apply(instance, Fraction::getFraction));

    private ItematicCodecs() {}

    public static Codec<Integer> index(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive: " + size);
        }
        return Codec.INT.validate(i -> {
            if (i >= 0 && i <= size) {
                return DataResult.success(i);
            }
            return DataResult.error(() -> "Index must be non-negative and less than " + size + ": " + i);
        });
    }

    public static Codec<Float> positiveFloat(float maxInclusive) {
        if (maxInclusive <= 0.0f) {
            throw new IllegalArgumentException("maxInclusive must be positive, got " + maxInclusive + " instead");
        }
        return CodecsAccessor.rangedFloat(0.0f, maxInclusive, value -> "Value must be positive and at most " + maxInclusive + ": " + value);
    }

    public static Codec<Fraction> positiveFraction(int maxInclusive) {
        if (maxInclusive <= 0) {
            throw new IllegalArgumentException("maxInclusive must be positive, got " + maxInclusive + " instead");
        }
        return POSITIVE_FRACTION.validate(fraction -> {
            if (fraction.intValue() > maxInclusive) {
                return DataResult.error(() -> "Fraction must be at most " + maxInclusive);
            }
            return DataResult.success(fraction);
        });
    }
}
