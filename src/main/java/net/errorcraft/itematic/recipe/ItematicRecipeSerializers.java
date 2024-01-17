package net.errorcraft.itematic.recipe;

import net.minecraft.recipe.RecipeSerializer;

public class ItematicRecipeSerializers {
    public static final RecipeSerializer<ItemColoringRecipe> ITEM_COLORING = RecipeSerializer.register("item_coloring", new ItemColoringRecipe.Serializer());

    private ItematicRecipeSerializers() {}

    public static void init() {}
}
