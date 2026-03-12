package net.errorcraft.itematic.mixin.client.render.entity;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.render.entity.WitchEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WitchEntityRenderer.class)
public class WitchEntityRendererExtender {
    @Redirect(
        method = "updateRenderState(Lnet/minecraft/entity/mob/WitchEntity;Lnet/minecraft/client/render/entity/state/WitchEntityRenderState;F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForPotionUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.POTION);
    }
}
