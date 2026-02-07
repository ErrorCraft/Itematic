package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.featuretoggle.FeatureSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerRecipeManager.class)
public class ServerRecipeManagerExtender {
    @Shadow
    @Final
    private RegistryWrapper.WrapperLookup registries;

    @Unique
    private static RegistryWrapper.Impl<Item> tempItemLookup;

    @Inject(
        method = "initialize",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/ServerRecipeManager;collectServerRecipes(Ljava/lang/Iterable;Lnet/minecraft/resource/featuretoggle/FeatureSet;)Ljava/util/List;"
        )
    )
    private void setTempItemLookup(FeatureSet features, CallbackInfo info) {
        tempItemLookup = this.registries.getOrThrow(RegistryKeys.ITEM);
    }

    @Redirect(
        method = "collectServerRecipes",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Recipe;getDisplays()Ljava/util/List;"
        )
    )
    private static List<RecipeDisplay> getDisplaysUseDynamicRegistry(Recipe<?> instance) {
        return ((RecipeAccess) instance).itematic$displays(tempItemLookup);
    }

    @Inject(
        method = "initialize",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/ServerRecipeManager;collectServerRecipes(Ljava/lang/Iterable;Lnet/minecraft/resource/featuretoggle/FeatureSet;)Ljava/util/List;",
            shift = At.Shift.AFTER
        )
    )
    private void resetTempItemLookup(FeatureSet features, CallbackInfo info) {
        tempItemLookup = null;
    }
}
