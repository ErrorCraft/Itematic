package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.BrushItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BrushItem.class)
public class BrushItemExtender {
    @Redirect(
        method = "usageTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/BrushItem;getMaxUseTime(Lnet/minecraft/item/ItemStack;)I"
        )
    )
    private int getMaxUseTimeUseItemStackVersion(BrushItem instance, ItemStack stack) {
        return stack.getMaxUseTime();
    }
}
