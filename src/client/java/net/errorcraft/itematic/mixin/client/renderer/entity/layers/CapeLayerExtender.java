package net.errorcraft.itematic.mixin.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CapeLayer.class)
public class CapeLayerExtender {
    @WrapMethod(
        method = "hasLayer"
    )
    private boolean alsoCheckEquipmentItemBehavior(ItemStack stack, EquipmentClientInfo.LayerType layerType, Operation<Boolean> original) {
        if (!stack.itematic$hasBehavior(ItemBehaviorType.EQUIPMENT)) {
            return false;
        }

        return original.call(stack, layerType);
    }
}
