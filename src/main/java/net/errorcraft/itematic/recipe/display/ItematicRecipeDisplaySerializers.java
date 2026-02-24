package net.errorcraft.itematic.recipe.display;

import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ItematicRecipeDisplaySerializers {
    public static final RecipeDisplay.Serializer<BrewingRecipeDisplay> BREWING = register("brewing", new RecipeDisplay.Serializer<>(BrewingRecipeDisplay.CODEC, BrewingRecipeDisplay.PACKET_CODEC));

    private ItematicRecipeDisplaySerializers() {}

    public static void init() {}

    private static <T extends RecipeDisplay> RecipeDisplay.Serializer<T> register(String id, RecipeDisplay.Serializer<T> serializer) {
        return Registry.register(Registries.RECIPE_DISPLAY, id, serializer);
    }
}
