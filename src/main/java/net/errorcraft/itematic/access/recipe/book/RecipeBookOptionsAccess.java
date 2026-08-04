package net.errorcraft.itematic.access.recipe.book;

import net.minecraft.stats.RecipeBookSettings;

public interface RecipeBookOptionsAccess {
    default RecipeBookSettings.TypeSettings itematic$brewing() {
        return null;
    }
    default void itematic$setBrewing(RecipeBookSettings.TypeSettings brewing) {}
}
