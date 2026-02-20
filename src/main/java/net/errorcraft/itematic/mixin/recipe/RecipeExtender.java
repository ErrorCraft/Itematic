package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.minecraft.item.Item;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.registry.RegistryEntryLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Recipe.class)
public interface RecipeExtender extends RecipeAccess {
    @Shadow
    IngredientPlacement getIngredientPlacement();

    @Shadow
    List<RecipeDisplay> getDisplays();

    @Override
    default IngredientPlacement itematic$ingredientPlacement(RegistryEntryLookup<Item> items) {
        return this.getIngredientPlacement();
    }

    @Override
    default List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        return this.getDisplays();
    }
}
