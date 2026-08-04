package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.access.recipe.RecipeFinderAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlaceRecipe.class)
public class InputSlotFillerExtender<R extends Recipe<?>> {
    @Shadow
    @Final
    private Inventory inventory;

    @ModifyExpressionValue(
        method = "placeRecipe(Lnet/minecraft/recipebook/ServerPlaceRecipe$CraftingMenuAccess;IILjava/util/List;Ljava/util/List;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/crafting/RecipeHolder;ZZ)Lnet/minecraft/world/inventory/RecipeBookMenu$PostPlaceAction;",
        at = @At(
            value = "NEW",
            target = "()Lnet/minecraft/world/entity/player/StackedItemContents;"
        )
    )
    private static StackedItemContents recipeFinderSetWorld(StackedItemContents original, @Local(argsOnly = true) Inventory inventory) {
        ((RecipeFinderAccess) original).itematic$setWorld(inventory.player.level());
        return original;
    }

    @Redirect(
        method = "placeRecipe(Lnet/minecraft/world/item/crafting/RecipeHolder;Lnet/minecraft/world/entity/player/StackedItemContents;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Recipe;placementInfo()Lnet/minecraft/world/item/crafting/PlacementInfo;"
        )
    )
    private PlacementInfo getIngredientPlacementUseDynamicRegistry(R instance) {
        return ((RecipeAccess) instance).itematic$ingredientPlacement(this.inventory.player.registryAccess().lookupOrThrow(Registries.ITEM));
    }
}
