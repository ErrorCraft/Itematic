package net.errorcraft.itematic.world.item.crafting;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeBookCategory;

public class ItematicRecipeBookCategories {
    public static final RecipeBookCategory BREWING_MODIFY = register("brewing_modify");
    public static final RecipeBookCategory BREWING_AMPLIFY = register("brewing_amplify");

    private ItematicRecipeBookCategories() {}

    public static void init() {}

    private static RecipeBookCategory register(String id) {
        return Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, id, new RecipeBookCategory());
    }
}
