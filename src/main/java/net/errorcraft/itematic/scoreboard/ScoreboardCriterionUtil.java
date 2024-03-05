package net.errorcraft.itematic.scoreboard;

import net.minecraft.registry.RegistryWrapper;

public class ScoreboardCriterionUtil {
    private static RegistryWrapper.WrapperLookup lookup;

    private ScoreboardCriterionUtil() {}

    public static RegistryWrapper.WrapperLookup lookup() {
        return lookup;
    }

    public static void setLookup(RegistryWrapper.WrapperLookup lookup) {
        ScoreboardCriterionUtil.lookup = lookup;
    }
}
