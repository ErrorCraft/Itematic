package net.errorcraft.itematic.util;

import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.math.Fraction;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Util {
    private Util() {}

    public static String stackTraceMessage(String message) {
        return Arrays.stream(Thread.currentThread().getStackTrace())
            .map(Objects::toString)
            .collect(Collectors.joining("\n\t", message + "\nStack trace:\n\t", ""));
    }

    public static int multiplyFraction(Fraction fraction, int multiplier) {
        return MathHelper.ceil(fraction.multiplyBy(Fraction.getFraction(multiplier, 1)).doubleValue());
    }
}
