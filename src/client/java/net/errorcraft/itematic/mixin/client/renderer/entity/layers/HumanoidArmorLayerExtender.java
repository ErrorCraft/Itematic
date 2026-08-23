package net.errorcraft.itematic.mixin.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerExtender<S extends HumanoidRenderState> {
    @WrapMethod(
        method = "shouldRender(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z"
    )
    private static boolean alsoCheckEquipmentItemBehavior(ItemStack itemStack, EquipmentSlot slot, Operation<Boolean> original) {
        if (!itemStack.itematic$hasBehavior(ItemBehaviorType.EQUIPMENT)) {
            return false;
        }

        return original.call(itemStack, slot);
    }

    @WrapMethod(
        method = "renderArmorPiece"
    )
    private void alsoCheckEquipmentItemBehavior(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, EquipmentSlot slot, int lightCoords, S state, Operation<Void> original) {
        if (!itemStack.itematic$hasBehavior(ItemBehaviorType.EQUIPMENT)) {
            return;
        }

        original.call(poseStack, submitNodeCollector, itemStack, slot, lightCoords, state);
    }
}
