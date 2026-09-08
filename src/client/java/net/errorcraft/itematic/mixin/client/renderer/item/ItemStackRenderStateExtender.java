package net.errorcraft.itematic.mixin.client.renderer.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.errorcraft.itematic.access.client.renderer.item.ItemStackRenderStateAccess;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStackRenderState.class)
public class ItemStackRenderStateExtender implements ItemStackRenderStateAccess {
    @Unique
    private static final Identifier FAILED_TO_LOAD_TEXTURE = Identifier.withDefaultNamespace("textures/gui/sprites/item/failed_to_load.png");

    @Unique
    private static final RenderType FAILED_TO_LOAD_RENDER_TYPE = RenderTypes.entityCutoutCull(FAILED_TO_LOAD_TEXTURE);

    @Unique
    private boolean successfullyLoaded = true;

    @ModifyReturnValue(
        method = "isEmpty",
        at = @At("TAIL")
    )
    private boolean checkSuccessfullyLoaded(boolean original) {
        return original && this.successfullyLoaded;
    }

    @Override
    public boolean itematic$trySubmitUnloadable(PoseStack poseStack, boolean shift, boolean shrink, @Nullable Quaternionf facingOrientation, SubmitNodeCollector submitNodeCollector, int lightCoords) {
        if (this.successfullyLoaded) {
            return false;
        }

        poseStack.pushPose();
        if (shift) {
            poseStack.translate(0.0f, 0.5f, 0.0f);
        }

        if (shrink) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        }

        if (facingOrientation != null) {
            poseStack.mulPose(facingOrientation);
        }

        submitNodeCollector.submitCustomGeometry(
            poseStack,
            FAILED_TO_LOAD_RENDER_TYPE,
            (pose, builder) -> {
                vertex(builder, pose, 0.0f, 0.0f, 0, 1, lightCoords);
                vertex(builder, pose, 1.0f, 0.0f, 1, 1, lightCoords);
                vertex(builder, pose, 1.0f, 1.0f, 1, 0, lightCoords);
                vertex(builder, pose, 0.0f, 1.0f, 0, 0, lightCoords);
            }
        );
        poseStack.popPose();
        return true;
    }

    @Override
    public void itematic$setSuccessfullyLoaded(boolean successfullyLoaded) {
        this.successfullyLoaded = successfullyLoaded;
    }

    @Unique
    private static void vertex(VertexConsumer builder, PoseStack.Pose pose, float x, float y, int u, int v, int lightCoords) {
        builder.addVertex(pose, x - 0.5f, y - 0.5f, 0.0f)
            .setColor(0xffffffff)
            .setUv(u, v)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(lightCoords)
            .setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
}
