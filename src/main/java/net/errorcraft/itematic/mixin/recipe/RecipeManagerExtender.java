package net.errorcraft.itematic.mixin.recipe;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerExtender {
    @Shadow
    @Final
    private RegistryWrapper.WrapperLookup registryLookup;

    @Inject(
        method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;",
            remap = false
        )
    )
    private void initializeRecipe(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info, @Local RecipeEntry<?> recipeEntry) {
        this.initRecipe(recipeEntry.value());
    }

    @Inject(
        method = "setRecipes",
        at = @At("TAIL")
    )
    private void initializeRecipes(Iterable<RecipeEntry<?>> recipeEntries, CallbackInfo info) {
        for (RecipeEntry<?> recipeEntry : recipeEntries) {
            this.initRecipe(recipeEntry.value());
        }
    }

    @Unique
    private void initRecipe(Recipe<?> recipe) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.itematic$initMatchingStacks(this.registryLookup);
        }
    }
}
