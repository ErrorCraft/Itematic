package net.errorcraft.itematic.recipe;

import net.minecraft.recipe.BrewingRecipeRegistry;

public class BrewingRecipeRegistryUtil {
    @SuppressWarnings("InstantiationOfUtilityClass")
    public static final BrewingRecipeRegistry INSTANCE = new BrewingRecipeRegistry();

    private BrewingRecipeRegistryUtil() {}
}
