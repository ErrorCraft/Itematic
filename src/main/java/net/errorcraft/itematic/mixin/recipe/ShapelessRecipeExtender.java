package net.errorcraft.itematic.mixin.recipe;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ShapelessRecipe.class)
public class ShapelessRecipeExtender {
    @ModifyVariable(
        method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z",
        at = @At("STORE")
    )
    private RecipeMatcher matchesRecipeMatcherSetRegistryManager(RecipeMatcher value, RecipeInputInventory recipeInputInventory, World world) {
        value.setRegistryManager(world.getRegistryManager());
        return value;
    }
}
