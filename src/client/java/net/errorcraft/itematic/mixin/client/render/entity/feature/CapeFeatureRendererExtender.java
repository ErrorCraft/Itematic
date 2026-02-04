package net.errorcraft.itematic.mixin.client.render.entity.feature;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererExtender {
    @Inject(
        method = "method_64075",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceEquipmentBehavior(ItemStack itemStack, CallbackInfoReturnable<Boolean> info) {
        if (!itemStack.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            info.setReturnValue(false);
        }
    }
}
