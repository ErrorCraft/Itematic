package net.errorcraft.itematic.recipe;

import net.errorcraft.itematic.recipe.brewing.AmplifyBrewingRecipe;
import net.errorcraft.itematic.recipe.brewing.ModifyBrewingRecipe;
import net.minecraft.recipe.RecipeSerializer;

public class ItematicRecipeSerializers {
    public static final RecipeSerializer<ModifyBrewingRecipe> BREWING_MODIFY = RecipeSerializer.register("brewing_modify", new ModifyBrewingRecipe.Serializer());
    public static final RecipeSerializer<AmplifyBrewingRecipe> BREWING_AMPLIFY = RecipeSerializer.register("brewing_amplify", new AmplifyBrewingRecipe.Serializer());

    private ItematicRecipeSerializers() {}

    public static void init() {}
}
