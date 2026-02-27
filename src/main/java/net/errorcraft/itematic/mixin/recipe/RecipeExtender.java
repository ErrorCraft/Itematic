package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Recipe.class)
public interface RecipeExtender extends RecipeAccess {
    @Shadow
    DefaultedList<Ingredient> getIngredients();

    @Override
    default DefaultedList<Ingredient> itematic$ingredients(RegistryEntryLookup<Item> items) {
        return this.getIngredients();
    }
}
