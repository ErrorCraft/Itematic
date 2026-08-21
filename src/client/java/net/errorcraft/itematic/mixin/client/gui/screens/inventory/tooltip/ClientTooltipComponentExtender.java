package net.errorcraft.itematic.mixin.client.gui.screens.inventory.tooltip;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientTooltipComponent.class)
public interface ClientTooltipComponentExtender {
    @ModifyExpressionValue(
        method = "create(Lnet/minecraft/world/inventory/tooltip/TooltipComponent;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/item/component/BundleContents;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientBundleTooltip;"
        )
    )
    private static ClientBundleTooltip setCapacity(ClientBundleTooltip original, @Local BundleTooltip data) {
        original.itematic$setCapacity(data.itematic$capacity());
        return original;
    }
}
