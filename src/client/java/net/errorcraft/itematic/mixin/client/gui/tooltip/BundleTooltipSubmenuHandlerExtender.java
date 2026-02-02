package net.errorcraft.itematic.mixin.client.gui.tooltip;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.gui.tooltip.BundleTooltipSubmenuHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BundleTooltipSubmenuHandler.class)
public class BundleTooltipSubmenuHandlerExtender {
    @Redirect(
        method = "isApplicableTo",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBundleUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasComponent(ItemComponentTypes.ITEM_HOLDER);
    }
}
