package net.errorcraft.itematic.scoreboard;

import net.minecraft.registry.RegistryWrapper;

public class ScoreboardScoreUtil {
    private static RegistryWrapper.WrapperLookup lookup;

    private ScoreboardScoreUtil() {}

    public static RegistryWrapper.WrapperLookup lookup() {
        return lookup;
    }

    public static void setLookup(RegistryWrapper.WrapperLookup lookup) {
        ScoreboardScoreUtil.lookup = lookup;
    }
}
