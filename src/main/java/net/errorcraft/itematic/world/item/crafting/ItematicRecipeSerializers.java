package net.errorcraft.itematic.world.item.crafting;

import net.minecraft.world.item.crafting.RecipeSerializer;

public class ItematicRecipeSerializers {
    public static final RecipeSerializer<ModifyBrewingRecipe> BREWING_MODIFY = RecipeSerializer.register("brewing_modify", new ModifyBrewingRecipe.Serializer());
    public static final RecipeSerializer<AmplifyBrewingRecipe> BREWING_AMPLIFY = RecipeSerializer.register("brewing_amplify", new AmplifyBrewingRecipe.Serializer());

    private ItematicRecipeSerializers() {}

    public static void init() {}
}
