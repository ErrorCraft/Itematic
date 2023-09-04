package net.errorcraft.itematic.access.recipe;

import net.errorcraft.itematic.recipe.CuttingRecipeFactory;
import net.minecraft.recipe.CuttingRecipe;

public interface CuttingRecipeSerializerAccess<T extends CuttingRecipe> {
    default void setFactory(CuttingRecipeFactory<T> factory) {}
}
