package net.errorcraft.itematic.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererExtender {
    @WrapOperation(
        method = "renderItemActivationAnimation",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"
        )
    )
    private void trySubmitUnloadable(ItemStackRenderState instance, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor, Operation<Void> original) {
        if (instance.itematic$trySubmitUnloadable(poseStack, false, false, null, submitNodeCollector, lightCoords)) {
            return;
        }

        original.call(instance, poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
    }
}
