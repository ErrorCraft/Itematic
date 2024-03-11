package net.errorcraft.itematic.mixin.client.gui.tooltip;

import net.errorcraft.itematic.access.client.gui.tooltip.BundleTooltipComponentAccess;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BundleTooltipComponent.class)
public class BundleTooltipComponentExtender implements BundleTooltipComponentAccess {
    @Unique
    private int capacity;

    @ModifyConstant(
        method = "drawItems",
        constant = @Constant(
            intValue = 64
        )
    )
    private int getCapacity(int constant) {
        return this.capacity;
    }

    @Override
    public void itematic$setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
