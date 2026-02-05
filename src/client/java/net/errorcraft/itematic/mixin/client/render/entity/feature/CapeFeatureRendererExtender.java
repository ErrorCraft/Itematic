package net.errorcraft.itematic.mixin.client.render.entity.feature;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererExtender {
    @Inject(
        method = "hasCustomModelForLayer",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceEquipmentBehavior(ItemStack stack, EquipmentModel.LayerType layerType, CallbackInfoReturnable<Boolean> info) {
        if (!stack.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            info.setReturnValue(false);
        }
    }
}
