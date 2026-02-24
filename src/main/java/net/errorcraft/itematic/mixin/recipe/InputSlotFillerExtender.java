package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.access.recipe.RecipeFinderAccess;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.InputSlotFiller;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InputSlotFiller.class)
public class InputSlotFillerExtender<R extends Recipe<?>> {
    @Shadow
    @Final
    private PlayerInventory inventory;

    @ModifyExpressionValue(
        method = "fill(Lnet/minecraft/recipe/InputSlotFiller$Handler;IILjava/util/List;Ljava/util/List;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/recipe/RecipeEntry;ZZ)Lnet/minecraft/screen/AbstractRecipeScreenHandler$PostFillAction;",
        at = @At(
            value = "NEW",
            target = "()Lnet/minecraft/recipe/RecipeFinder;"
        )
    )
    private static RecipeFinder recipeFinderSetWorld(RecipeFinder original, @Local(argsOnly = true) PlayerInventory inventory) {
        ((RecipeFinderAccess) original).itematic$setWorld(inventory.player.getWorld());
        return original;
    }

    @Redirect(
        method = "fill(Lnet/minecraft/recipe/RecipeEntry;Lnet/minecraft/recipe/RecipeFinder;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Recipe;getIngredientPlacement()Lnet/minecraft/recipe/IngredientPlacement;"
        )
    )
    private IngredientPlacement getIngredientPlacementUseDynamicRegistry(R instance) {
        return ((RecipeAccess) instance).itematic$ingredientPlacement(this.inventory.player.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ITEM));
    }
}
