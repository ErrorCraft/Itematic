package net.errorcraft.itematic.util;

import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Util {
    private Util() {}

    public static String descriptionKey(String prefix, Identifier id, String suffix) {
        return net.minecraft.util.Util.createTranslationKey(prefix, id) + "." + suffix;
    }

    public static String stackTraceMessage(String message) {
        return Arrays.stream(Thread.currentThread().getStackTrace())
            .map(Objects::toString)
            .collect(Collectors.joining("\n\t", message + "\nStack trace:\n\t", ""));
    }
}
