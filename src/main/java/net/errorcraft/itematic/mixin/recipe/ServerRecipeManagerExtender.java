package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RecipeManager.class)
public class ServerRecipeManagerExtender {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @Unique
    private static HolderLookup.RegistryLookup<Item> tempItemLookup;

    @Inject(
        method = "finalizeRecipeLoading",
        at = @At("HEAD")
    )
    private void setTempItemLookup(FeatureFlagSet features, CallbackInfo info) {
        tempItemLookup = this.registries.lookupOrThrow(Registries.ITEM);
    }

    @Redirect(
        method = {
            "method_64989",
            "unpackRecipeInfo"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Recipe;placementInfo()Lnet/minecraft/world/item/crafting/PlacementInfo;"
        )
    )
    private static PlacementInfo getIngredientPlacementUseDynamicRegistry(Recipe<?> instance) {
        return ((RecipeAccess) instance).itematic$ingredientPlacement(tempItemLookup);
    }

    @Redirect(
        method = "unpackRecipeInfo",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Recipe;display()Ljava/util/List;"
        )
    )
    private static List<RecipeDisplay> getDisplaysUseDynamicRegistry(Recipe<?> instance) {
        return ((RecipeAccess) instance).itematic$displays(tempItemLookup);
    }

    @Inject(
        method = "finalizeRecipeLoading",
        at = @At("RETURN")
    )
    private void resetTempItemLookup(FeatureFlagSet features, CallbackInfo info) {
        tempItemLookup = null;
    }
}
