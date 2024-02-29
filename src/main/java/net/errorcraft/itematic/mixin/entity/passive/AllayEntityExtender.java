package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AllayEntity.class)
public class AllayEntityExtender {
    @Redirect(
        method = "matchesDuplicationIngredient",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean testForDuplicationItemUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.itematic$isOf(ItemKeys.AMETHYST_SHARD);
    }
}
