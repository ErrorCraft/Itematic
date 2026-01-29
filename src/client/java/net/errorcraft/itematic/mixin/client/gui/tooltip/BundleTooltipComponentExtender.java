package net.errorcraft.itematic.mixin.client.gui.tooltip;

import net.errorcraft.itematic.access.client.gui.tooltip.BundleTooltipComponentAccess;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import org.apache.commons.lang3.math.Fraction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BundleTooltipComponent.class)
public class BundleTooltipComponentExtender implements BundleTooltipComponentAccess {
    @Unique
    private Fraction capacity;

    @Redirect(
        method = "getProgressBarLabel",
        at = @At(
            value = "FIELD",
            target = "Lorg/apache/commons/lang3/math/Fraction;ONE:Lorg/apache/commons/lang3/math/Fraction;",
            opcode = Opcodes.GETSTATIC,
            remap = false
        )
    )
    private Fraction getCapacity() {
        return this.capacity;
    }

    @Override
    public void itematic$setCapacity(Fraction capacity) {
        this.capacity = capacity;
    }
}
