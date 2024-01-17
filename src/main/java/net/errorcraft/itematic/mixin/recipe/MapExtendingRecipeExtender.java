package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.MapExtendingRecipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MapExtendingRecipe.class)
public class MapExtendingRecipeExtender {
    @Unique
    private static final int MAP_SLOT = 4;

    @Redirect(
        method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/ShapedRecipe;matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z"
        )
    )
    private boolean matchesUseRegistryKeyCheck(ShapedRecipe instance, RecipeInputInventory recipeInputInventory, World world) {
        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack stack = recipeInputInventory.getStack(i);
            if (!isValid(stack, i)) {
                return false;
            }
        }
        return true;
    }

    @Unique
    private static boolean isValid(ItemStack stack, int index) {
        if (index == MAP_SLOT) {
            return stack.itematic$isOf(ItemKeys.FILLED_MAP);
        }
        return stack.itematic$isOf(ItemKeys.PAPER);
    }
}
