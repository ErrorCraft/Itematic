package net.errorcraft.itematic.mixin.recipe;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SmithingTransformRecipe.class)
public class SmithingTransformRecipeExtender {
    @Shadow
    @Final
    ItemStack result;

    @Redirect(
        method = "craft",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;copyComponentsToNewStack(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack copyComponentsToNewStackUseRegistryEntry(ItemStack instance, ItemConvertible item, int count) {
        return instance.itematic$copyComponentsToNewStack(this.result.getRegistryEntry(), count);
    }
}
