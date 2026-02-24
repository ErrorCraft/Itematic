package net.errorcraft.itematic.recipe.book;

import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ItematicRecipeBookCategories {
    public static final RecipeBookCategory BREWING_MODIFY = register("brewing_modify");
    public static final RecipeBookCategory BREWING_AMPLIFY = register("brewing_amplify");

    private ItematicRecipeBookCategories() {}

    public static void init() {}

    private static RecipeBookCategory register(String id) {
        return Registry.register(Registries.RECIPE_BOOK_CATEGORY, id, new RecipeBookCategory());
    }
}
