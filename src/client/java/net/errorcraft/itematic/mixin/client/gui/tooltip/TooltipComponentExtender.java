package net.errorcraft.itematic.mixin.client.gui.tooltip;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.client.gui.tooltip.BundleTooltipComponentAccess;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.BundleTooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TooltipComponent.class)
public interface TooltipComponentExtender {
    @ModifyExpressionValue(
        method = "of(Lnet/minecraft/client/item/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/component/type/BundleContentsComponent;)Lnet/minecraft/client/gui/tooltip/BundleTooltipComponent;"
        )
    )
    private static BundleTooltipComponent setCapacity(BundleTooltipComponent original, @Local BundleTooltipData data) {
        ((BundleTooltipComponentAccess) original).itematic$setCapacity(data.itematic$capacity());
        return original;
    }
}
