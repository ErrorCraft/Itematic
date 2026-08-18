package net.errorcraft.itematic.world.item.crafting;

import net.minecraft.world.item.crafting.RecipeType;

public class ItematicRecipeTypes {
    public static final RecipeType<BrewingRecipe<?>> BREWING = RecipeType.register("brewing");

    private ItematicRecipeTypes() {}

    public static void init() {}
}
