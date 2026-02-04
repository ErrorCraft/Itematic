package net.errorcraft.itematic.mixin.client.render.entity.feature;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.WolfArmorFeatureRenderer;
import net.minecraft.client.render.entity.state.WolfEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfArmorFeatureRenderer.class)
public class WolfArmorFeatureRendererExtender {
    @Inject(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/WolfEntityRenderState;FF)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceEquipmentBehavior(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, WolfEntityRenderState wolfEntityRenderState, float f, float g, CallbackInfo info) {
        if (!wolfEntityRenderState.bodyArmor.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            info.cancel();
        }
    }
}
