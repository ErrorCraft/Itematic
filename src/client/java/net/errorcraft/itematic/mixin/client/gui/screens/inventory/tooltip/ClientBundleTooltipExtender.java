package net.errorcraft.itematic.mixin.client.gui.screens.inventory.tooltip;

import net.errorcraft.itematic.access.client.gui.screens.inventory.tooltip.ClientBundleTooltipAccess;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import org.apache.commons.lang3.math.Fraction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientBundleTooltip.class)
public class ClientBundleTooltipExtender implements ClientBundleTooltipAccess {
    @Unique
    private Fraction capacity;

    @Redirect(
        method = "getProgressBarFillText",
        at = @At(
            value = "FIELD",
            target = "Lorg/apache/commons/lang3/math/Fraction;ONE:Lorg/apache/commons/lang3/math/Fraction;",
            opcode = Opcodes.GETSTATIC,
            remap = false
        )
    )
    private Fraction capacity() {
        return this.capacity;
    }

    @Override
    public void itematic$setCapacity(Fraction capacity) {
        this.capacity = capacity;
    }
}
