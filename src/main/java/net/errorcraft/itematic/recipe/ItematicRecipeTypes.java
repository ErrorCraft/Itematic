package net.errorcraft.itematic.recipe;

import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public class ItematicRecipeTypes {
    public static final RecipeType<BrewingRecipe<?>> BREWING = RecipeType.register("brewing");

    private ItematicRecipeTypes() {}

    public static void init() {}
}
