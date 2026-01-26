package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.IngredientAccess;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Ingredient.class)
public class IngredientExtender implements IngredientAccess {
    // TODO see IngredientAccess
}
