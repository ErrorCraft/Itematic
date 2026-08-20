package net.errorcraft.itematic.mixin.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WingsLayer.class)
public class WingsLayerExtender<S extends HumanoidRenderState> {
    @WrapMethod(
        method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V"
    )
    private void checkEquipmentItemBehavior(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot, Operation<Void> original) {
        if (!state.chestEquipment.itematic$hasBehavior(ItemBehaviorType.EQUIPMENT)) {
            return;
        }

        original.call(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
    }
}
