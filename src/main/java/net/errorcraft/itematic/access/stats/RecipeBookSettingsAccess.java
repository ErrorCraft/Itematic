package net.errorcraft.itematic.access.stats;

import net.minecraft.stats.RecipeBookSettings;

public interface RecipeBookSettingsAccess {
    default RecipeBookSettings.TypeSettings itematic$brewing() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setBrewing(RecipeBookSettings.TypeSettings brewing) {}
}
