package net.errorcraft.itematic.mixin.world.item;

import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BoneMealItem.class)
public class BoneMealItemExtender {
    @Redirect(
        method = {
            "growCrop",
            "growWaterPlant"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
        )
    )
    private static void doNotDecrementItemStack(ItemStack instance, int amount) {}
}
