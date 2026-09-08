package net.errorcraft.itematic.mixin.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererExtender extends EntityRenderer<ItemEntity, ItemEntityRenderState> {
    protected ItemEntityRendererExtender(EntityRendererProvider.Context context) {
        super(context);
    }

    @WrapMethod(
        method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"
    )
    private void trySubmitUnloadable(ItemEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, Operation<Void> original) {
        if (state.item.itematic$trySubmitUnloadable(poseStack, true, true, camera.orientation, submitNodeCollector, state.lightCoords)) {
            super.submit(state, poseStack, submitNodeCollector, camera);
            return;
        }

        original.call(state, poseStack, submitNodeCollector, camera);
    }
}
