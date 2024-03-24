package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RepairItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeExtender {
    @Redirect(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseRegistryEntry(ItemConvertible item, @Local(ordinal = 0) ItemStack firstStack) {
        return new ItemStack(firstStack.getRegistryEntry());
    }
}
