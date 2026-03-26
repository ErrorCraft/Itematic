package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BoneMealItem.class)
public class BoneMealItemExtender {
    @Redirect(
        method = {
            "useOnFertilizable",
            "useOnGround"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;decrement(I)V"
        )
    )
    private static void doNotDecrementItemStack(ItemStack instance, int amount) {}
}
