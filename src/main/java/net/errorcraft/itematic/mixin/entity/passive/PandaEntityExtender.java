package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PandaEntity.class)
public class PandaEntityExtender {
    @Redirect(
        method = "method_6504",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private static boolean isOfForFoodUseItemTagCheckStatic(ItemStack instance, Item item) {
        return instance.isIn(ItematicItemTags.PANDA_EAT_ITEMS);
    }

    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean testForFoodItemsUseItemTagCheck(ItemStack instance, Item item) {
        return instance.isIn(ItematicItemTags.PANDA_FOOD);
    }

    @Redirect(
        method = "canEat",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/PandaEntity;isBreedingItem(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean isOfForFoodUseItemTagCheck(PandaEntity instance, ItemStack stack) {
        return stack.isIn(ItematicItemTags.PANDA_EAT_ITEMS);
    }
}
