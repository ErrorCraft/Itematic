package net.errorcraft.itematic.mixin.client.gui.tooltip;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.gui.BundleMouseActions;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BundleMouseActions.class)
public class BundleTooltipSubmenuHandlerExtender {
    @Redirect(
        method = "matches",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
        )
    )
    private boolean isInForBundlesUseItemComponentCheck(ItemStack instance, TagKey<Item> tag) {
        return instance.itematic$hasBehavior(ItemComponentTypes.ITEM_HOLDER);
    }
}
