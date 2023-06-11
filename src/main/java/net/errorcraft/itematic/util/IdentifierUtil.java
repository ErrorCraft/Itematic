package net.errorcraft.itematic.util;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class IdentifierUtil {
    public static String createTranslationKey(RegistryKey<?> key, String prefix, String suffix) {
        return createTranslationKey(key.getValue(), prefix, suffix);
    }

    public static String createTranslationKey(Identifier id, String prefix, String suffix) {
        return Util.createTranslationKey(prefix, id) + "." + suffix;
    }
}
