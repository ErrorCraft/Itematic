package net.errorcraft.itematic.mixin.client.gui.tooltip;

import net.errorcraft.itematic.access.client.item.BundleTooltipDataAccess;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.client.item.BundleTooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BundleTooltipComponent.class)
public class BundleTooltipComponentExtender {
    @Unique
    private int capacity;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setCapacity(BundleTooltipData data, CallbackInfo info) {
        this.capacity = ((BundleTooltipDataAccess) data).itematic$capacity();
    }

    @ModifyConstant(
        method = "drawItems",
        constant = @Constant(
            intValue = 64
        )
    )
    private int getCapacity(int constant) {
        return this.capacity;
    }
}
