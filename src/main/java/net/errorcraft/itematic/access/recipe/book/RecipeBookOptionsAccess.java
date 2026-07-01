package net.errorcraft.itematic.access.recipe.book;

import net.minecraft.recipe.book.RecipeBookOptions;

public interface RecipeBookOptionsAccess {
    default RecipeBookOptions.CategoryOption itematic$brewing() {
        return null;
    }
    default void itematic$setBrewing(RecipeBookOptions.CategoryOption brewing) {}
}
