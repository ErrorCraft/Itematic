package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BrushItemComponent;
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
    private int getMaxUseTimeUseItemComponent(BrushItem instance, ItemStack stack) {
        return stack.itematic$getComponent(ItemComponentTypes.BRUSH)
            .map(BrushItemComponent::brushTicks)
            .orElseThrow();
    }
}
