package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.FireworkRocketRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketRecipe.class)
public class FireworkRocketRecipeExtender {
    @Redirect(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack craftNewItemStackUseRegistryEntry(ItemConvertible item, int count, RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager) {
        return new ItemStack(dynamicRegistryManager.get(RegistryKeys.ITEM).entryOf(ItemKeys.FIREWORK_ROCKET), count);
    }

    @Redirect(
        method = "getOutput",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack getOutputNewItemStackUseRegistryEntry(ItemConvertible item, DynamicRegistryManager registryManager) {
        return new ItemStack(registryManager.get(RegistryKeys.ITEM).entryOf(ItemKeys.FIREWORK_ROCKET));
    }
}
