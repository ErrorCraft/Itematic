package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.FireworkRocketRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(FireworkRocketRecipe.class)
public class FireworkRocketRecipeExtender {
    @Redirect(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0
        )
    )
    private boolean paperUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.itematic$isOf(ItemKeys.PAPER);
    }

    @Redirect(
        method = {
            "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
            "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;"
        },
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
        return itemStack.itematic$isOf(ItemKeys.GUNPOWDER);
    }

    @Redirect(
        method = {
            "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
            "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;"
        },
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
        return itemStack.itematic$isOf(ItemKeys.FIREWORK_STAR);
    }

    @Redirect(
        method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireworkRocketUseRegistryEntry(ItemConvertible item, int count, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        return lookup.getOrThrow(RegistryKeys.ITEM)
            .getOptional(ItemKeys.FIREWORK_ROCKET)
            .map(entry -> new ItemStack(entry, count))
            .orElse(ItemStack.EMPTY);
    }
}
