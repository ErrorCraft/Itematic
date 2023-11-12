package net.errorcraft.itematic.mixin.recipe;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.DynamicOps;
import net.errorcraft.itematic.access.recipe.RecipeManagerAccess;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerExtender implements RecipeManagerAccess {
    @Unique
    private Registry<Item> itemRegistry;

    @Unique
    private DynamicRegistryManager registryManager;

    @Unique
    private static DynamicRegistryManager tempRegistryManager;

    @Inject(
        method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
        at = @At("HEAD")
    )
    private void storeTempRegistryManager(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        tempRegistryManager = this.registryManager;
    }

    @Inject(
        method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
        at = @At("TAIL")
    )
    private void resetTempRegistryManager(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        tempRegistryManager = null;
    }

    @ModifyArg(
        method = "deserialize",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static <T> DynamicOps<T> useRegistryOps(DynamicOps<T> ops) {
        return RegistryOps.of(ops, tempRegistryManager);
    }

    @Inject(
        method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/recipe/RecipeManager;deserialize(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/RecipeEntry;",
            shift = At.Shift.AFTER
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

    @Override
    public void itematic$setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
        this.itemRegistry = registryManager.get(RegistryKeys.ITEM);
    }

    @Unique
    private void initRecipe(Recipe<?> recipe) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.itematic$initMatchingStacks(this.itemRegistry);
        }
    }
}
