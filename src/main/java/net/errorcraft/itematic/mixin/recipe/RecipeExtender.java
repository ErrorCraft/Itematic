package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.RecipeRemainderItemComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Recipe.class)
public interface RecipeExtender extends RecipeAccess {
    @Redirect(
        method = "getRemainder",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForRemainderUseItemComponent(ItemConvertible itemConvertible, @Local Item item) {
        return item.itematic$getComponent(ItemComponentTypes.RECIPE_REMAINDER)
            .map(RecipeRemainderItemComponent::item)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
    }
}
