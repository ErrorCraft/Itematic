package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Recipe.class)
public interface RecipeExtender extends RecipeAccess {
}
