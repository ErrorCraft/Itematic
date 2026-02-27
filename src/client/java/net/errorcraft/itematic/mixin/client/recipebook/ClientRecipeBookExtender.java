package net.errorcraft.itematic.mixin.client.recipebook;

import net.errorcraft.itematic.client.recipebook.ItematicRecipeBookGroups;
import net.errorcraft.itematic.recipe.brewing.AmplifyBrewingRecipe;
import net.errorcraft.itematic.recipe.brewing.ModifyBrewingRecipe;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookExtender {
    @Inject(
        method = "getGroupForRecipe",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void checkBrewingRecipes(RecipeEntry<?> recipe, CallbackInfoReturnable<RecipeBookGroup> info) {
        Recipe<?> actualRecipe = recipe.value();
        if (actualRecipe instanceof ModifyBrewingRecipe) {
            info.setReturnValue(ItematicRecipeBookGroups.BREWING_MODIFY);
            return;
        }

        if (actualRecipe instanceof AmplifyBrewingRecipe) {
            info.setReturnValue(ItematicRecipeBookGroups.BREWING_AMPLIFY);
        }
    }
}
