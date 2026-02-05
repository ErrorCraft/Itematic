package net.errorcraft.itematic.mixin.client.gui.tooltip;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.gui.tooltip.BundleTooltipSubmenuHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BundleTooltipSubmenuHandler.class)
public class BundleTooltipSubmenuHandlerExtender {
    @Redirect(
        method = "isApplicableTo",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
        )
    )
    private boolean isInForBundlesUseItemComponentCheck(ItemStack instance, TagKey<Item> tag) {
        return instance.itematic$hasBehavior(ItemComponentTypes.ITEM_HOLDER);
    }
}
