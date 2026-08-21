package net.errorcraft.itematic.world.item.crafting.display;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

public class ItematicRecipeDisplays {
    public static final RecipeDisplay.Type<BrewingRecipeDisplay> BREWING = register("brewing", new RecipeDisplay.Type<>(BrewingRecipeDisplay.CODEC, BrewingRecipeDisplay.STREAM_CODEC));

    private ItematicRecipeDisplays() {}

    public static void init() {}

    private static <T extends RecipeDisplay> RecipeDisplay.Type<T> register(String id, RecipeDisplay.Type<T> serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_DISPLAY, id, serializer);
    }
}
