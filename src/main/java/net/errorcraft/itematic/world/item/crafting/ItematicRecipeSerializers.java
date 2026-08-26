package net.errorcraft.itematic.world.item.crafting;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ItematicRecipeSerializers {
    public static final RecipeSerializer<ModifyBrewingRecipe> BREWING_MODIFY = register(
        "brewing_modify",
        ModifyBrewingRecipe.SERIALIZER
    );
    public static final RecipeSerializer<AmplifyBrewingRecipe> BREWING_AMPLIFY = register(
        "brewing_amplify",
        AmplifyBrewingRecipe.SERIALIZER
    );

    private ItematicRecipeSerializers() {}

    public static void init() {}

    private static <T extends Recipe<?>> RecipeSerializer<T> register(String id, RecipeSerializer<T> serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer);
    }
}
