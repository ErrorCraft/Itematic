package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmithingTrimRecipe;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SmithingTrimRecipe.class)
public class SmithingTrimRecipeExtender {
    @Redirect(
        method = "getResult",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForIronChestplateUseRegistryEntry(ItemConvertible item, RegistryWrapper.WrapperLookup lookup) {
        return lookup.getWrapperOrThrow(RegistryKeys.ITEM)
            .getOptional(ItemKeys.IRON_CHESTPLATE)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
    }
}
