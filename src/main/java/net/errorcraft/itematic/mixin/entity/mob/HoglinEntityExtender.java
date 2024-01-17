package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HoglinEntity.class)
public class HoglinEntityExtender {
    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean testForFoodItemsUseItemTagCheck(ItemStack instance, Item item) {
        return instance.isIn(ItematicItemTags.HOGLIN_FOOD);
    }
}
