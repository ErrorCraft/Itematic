package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
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
        method = "<init>",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForMapReturnEmptyStack(ItemConvertible item) {
        return ItemStack.EMPTY;
    }

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

    @Redirect(
        method = "findFilledMap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfForFilledMapUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasComponent(ItemComponentTypes.MAP_HOLDER);
    }

    @Unique
    private static boolean isValid(ItemStack stack, int index) {
        if (index == MAP_SLOT) {
            return stack.itematic$hasComponent(ItemComponentTypes.MAP_HOLDER);
        }
        return stack.itematic$isOf(ItemKeys.PAPER);
    }
}
