package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.item.BrushItemAccess;
import net.minecraft.item.BrushItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BrushItem.class)
public class BrushItemExtender implements BrushItemAccess {
    @Unique
    private int usedTicks;

    @ModifyVariable(
        method = "usageTick",
        at = @At("LOAD"),
        ordinal = 1
    )
    private int useUsedTicksDirectlyInsteadOfCalculating(int value, @Local(argsOnly = true) int remainingUseTicks) {
        return this.usedTicks + 1;
    }

    @Override
    public void itematic$setUsedTicks(int usedTicks) {
        this.usedTicks = usedTicks;
    }
}
