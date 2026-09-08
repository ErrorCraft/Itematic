package net.errorcraft.itematic.mixin.client.gui.render.pip;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.render.pip.OversizedItemRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OversizedItemRenderer.class)
public class OversizedItemRendererExtender {
    @WrapOperation(
        method = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/OversizedItemRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/item/TrackingItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"
        )
    )
    private void trySubmitUnloadable(TrackingItemStackRenderState instance, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor, Operation<Void> original) {
        if (instance.itematic$trySubmitUnloadable(poseStack, false, false, null, submitNodeCollector, lightCoords)) {
            return;
        }

        original.call(instance, poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
    }
}
