package net.errorcraft.itematic.mixin.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.state.DisplayEntityRenderState;
import net.minecraft.client.renderer.entity.state.ItemDisplayEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Display;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DisplayRenderer.class)
public class DisplayRendererExtender<T extends Display, S, ST extends DisplayEntityRenderState> {
    @Unique
    private static final ScopedValue<Quaternionf> CAMERA_ROTATION = ScopedValue.newInstance();

    @WrapOperation(
        method = "submit(Lnet/minecraft/client/renderer/entity/state/DisplayEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/DisplayRenderer;submitInner(Lnet/minecraft/client/renderer/entity/state/DisplayEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IF)V"
        )
    )
    private void passCameraRotation(DisplayRenderer<T, S, ST> instance, ST state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float interpolationProgress, Operation<Void> original, @Local(name = "camera", argsOnly = true) CameraRenderState camera) {
        ScopedValue.where(CAMERA_ROTATION, camera.orientation)
            .run(() -> original.call(instance, state, poseStack, submitNodeCollector, lightCoords, interpolationProgress));
    }

    @Mixin(DisplayRenderer.ItemDisplayRenderer.class)
    public static class ItemDisplayRendererExtender {
        @WrapMethod(
            method = "submitInner(Lnet/minecraft/client/renderer/entity/state/ItemDisplayEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IF)V"
        )
        private void trySubmitUnloadable(ItemDisplayEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float interpolationProgress, Operation<Void> original) {
            if (state.item.itematic$trySubmitUnloadable(poseStack, false, true, CAMERA_ROTATION.get(), submitNodeCollector, state.lightCoords)) {
                return;
            }

            original.call(state, poseStack, submitNodeCollector, lightCoords, interpolationProgress);
        }
    }
}
