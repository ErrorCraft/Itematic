package net.errorcraft.itematic.mixin.client.render.entity.feature;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererExtender {
    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/PlayerEntityRenderState;FF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForElytraUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.ELYTRA);
    }
}
