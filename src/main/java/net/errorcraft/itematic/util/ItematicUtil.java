package net.errorcraft.itematic.util;

import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.apache.commons.lang3.concurrent.Memoizer;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItematicUtil {
    private ItematicUtil() {}

    public static String descriptionKey(String prefix, Identifier id, String suffix) {
        return Util.makeDescriptionId(prefix, id) + "." + suffix;
    }

    public static String stackTraceMessage(String message) {
        return Arrays.stream(Thread.currentThread().getStackTrace())
            .map(Objects::toString)
            .collect(Collectors.joining("\n\t", message + "\nStack trace:\n\t", ""));
    }

    public static <I, O> Memoizer<I, O> memoize(Function<I, O> delegate) {
        return new Memoizer<>(delegate);
    }

    @Nullable
    public static <T, U, R> R applyNullable(@Nullable T first, @Nullable U second, BiFunction<T, U, R> operation) {
        if (first == null || second == null) {
            return null;
        }

        return operation.apply(first, second);
    }

    @Nullable
    public static <T, U> U applyNullable(@Nullable T value, Function<T, U> operation) {
        if (value == null) {
            return null;
        }

        return operation.apply(value);
    }
}
