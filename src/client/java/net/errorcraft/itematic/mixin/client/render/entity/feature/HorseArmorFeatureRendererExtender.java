package net.errorcraft.itematic.mixin.client.render.entity.feature;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HorseArmorFeatureRenderer;
import net.minecraft.client.render.entity.state.HorseEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseArmorFeatureRenderer.class)
public class HorseArmorFeatureRendererExtender {
    @Inject(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/HorseEntityRenderState;FF)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceEquipmentBehavior(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, HorseEntityRenderState horseEntityRenderState, float f, float g, CallbackInfo info) {
        if (!horseEntityRenderState.armor.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            info.cancel();
        }
    }
}
