package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.FireworkRocketRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(FireworkRocketRecipe.class)
public class FireworkRocketRecipeExtender {
    @Redirect(
        method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0
        )
    )
    private boolean paperUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isOf(ItemKeys.PAPER);
    }

    @Redirect(
        method = { "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z", "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/recipe/FireworkRocketRecipe;DURATION_MODIFIER:Lnet/minecraft/recipe/Ingredient;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean durationModifierUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isOf(ItemKeys.GUNPOWDER);
    }

    @Redirect(
        method = { "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z", "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/recipe/FireworkRocketRecipe;FIREWORK_STAR:Lnet/minecraft/recipe/Ingredient;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean fireworkStarUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isOf(ItemKeys.FIREWORK_STAR);
    }

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
