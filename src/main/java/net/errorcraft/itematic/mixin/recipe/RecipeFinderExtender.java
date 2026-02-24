package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.access.recipe.RecipeFinderAccess;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RecipeFinder.class)
public class RecipeFinderExtender implements RecipeFinderAccess {
    @Unique
    private World world;

    @WrapOperation(
        method = {
            "isCraftable(Lnet/minecraft/recipe/Recipe;ILnet/minecraft/recipe/RecipeMatcher$ItemCallback;)Z",
            "countCrafts(Lnet/minecraft/recipe/Recipe;ILnet/minecraft/recipe/RecipeMatcher$ItemCallback;)I"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Recipe;getIngredientPlacement()Lnet/minecraft/recipe/IngredientPlacement;"
        )
    )
    private IngredientPlacement getIngredientPlacementUseDynamicRegistry(Recipe<?> instance, Operation<IngredientPlacement> original) {
        if (this.world == null) {
            return original.call(instance);
        }

        return ((RecipeAccess) instance).itematic$ingredientPlacement(this.world.getRegistryManager().getOrThrow(RegistryKeys.ITEM));
    }

    @Override
    public void itematic$setWorld(World world) {
        this.world = world;
    }
}
