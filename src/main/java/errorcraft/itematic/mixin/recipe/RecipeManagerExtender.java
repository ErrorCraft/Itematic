package errorcraft.itematic.mixin.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import errorcraft.itematic.access.registry.DynamicRegistryManagerAccess;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerExtender implements DynamicRegistryManagerAccess {
    private DynamicRegistryManager registryManager;
    private Registry<Item> itemRegistry;

    @Inject(
        method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/recipe/RecipeManager;deserialize(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/Recipe;",
            shift = At.Shift.AFTER
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void applyInitializeRecipe(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci, Map<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>> map2, ImmutableMap.Builder<Identifier, Recipe<?>> builder, Iterator<Map.Entry<Identifier, JsonElement>> var6, Map.Entry<Identifier, JsonElement> entry, Identifier identifier, Recipe<?> recipe) {
        this.initRecipe(recipe);
    }

    @Inject(
        method = "setRecipes",
        at = @At("TAIL")
    )
    private void setRecipesInitalizeRecipes(Iterable<Recipe<?>> recipes, CallbackInfo info) {
        for (Recipe<?> recipe : recipes) {
            this.initRecipe(recipe);
        }
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
        this.itemRegistry = registryManager.get(RegistryKeys.ITEM);
    }

    private void initRecipe(Recipe<?> recipe) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.initMatchingStacks(this.itemRegistry);
        }
    }
}
