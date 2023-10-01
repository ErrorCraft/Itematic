package net.errorcraft.itematic.mixin.client.render.item;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererExtender {
    @Redirect(
        method = { "getHandRenderType", "getUsingItemHandRenderType" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;BOW:Lnet/minecraft/item/Item;"
            ),
            to = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CROSSBOW:Lnet/minecraft/item/Item;"
            )
        )
    )
    private static boolean isOfForBowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.BOW);
    }

    @Redirect(
        method = { "getHandRenderType", "getUsingItemHandRenderType", "isChargedCrossbow", "renderFirstPersonItem" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CROSSBOW:Lnet/minecraft/item/Item;"
            )
        )
    )
    private static boolean isOfForCrossbowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.CROSSBOW);
    }
}
