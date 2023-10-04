package net.errorcraft.itematic.mixin.screen;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class HorseScreenHandlerExtender {
    @Mixin(targets = "net/minecraft/screen/HorseScreenHandler$1")
    public static class SaddleSlotExtender {
        @Redirect(
            method = "canInsert",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
        )
        private boolean isOfForSaddleUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.isOf(ItemKeys.SADDLE);
        }
    }
}
