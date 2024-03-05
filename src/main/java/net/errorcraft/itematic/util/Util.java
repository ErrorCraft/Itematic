package net.errorcraft.itematic.util;

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
}
