package net.errorcraft.itematic.mixin.client.render.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.WolfArmorLayer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfArmorLayer.class)
public class WolfArmorFeatureRendererExtender {
    @Inject(
        method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/WolfRenderState;FF)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceEquipmentBehavior(PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, int i, WolfRenderState wolfEntityRenderState, float f, float g, CallbackInfo info) {
        if (!wolfEntityRenderState.bodyArmorItem.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            info.cancel();
        }
    }
}
