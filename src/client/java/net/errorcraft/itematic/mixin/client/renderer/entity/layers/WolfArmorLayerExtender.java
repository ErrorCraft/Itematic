package net.errorcraft.itematic.mixin.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.WolfArmorLayer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WolfArmorLayer.class)
public class WolfArmorLayerExtender {
    @WrapMethod(
        method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/WolfRenderState;FF)V"
    )
    private void alsoCheckEquipmentItemBehavior(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, WolfRenderState state, float yRot, float xRot, Operation<Void> original) {
        if (!state.bodyArmorItem.itematic$hasBehavior(ItemBehaviorType.EQUIPMENT)) {
            return;
        }

        original.call(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
    }
}
