package net.errorcraft.itematic.mixin.world.item;

import net.errorcraft.itematic.access.world.item.BrushItemAccess;
import net.minecraft.world.item.BrushItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BrushItem.class)
public class BrushItemExtender implements BrushItemAccess {
    @Unique
    private int usedTicks;

    @ModifyVariable(
        method = "onUseTick",
        at = @At("LOAD"),
        ordinal = 1
    )
    private int useUsedTicksDirectlyInsteadOfCalculating(int value) {
        return this.usedTicks + 1;
    }

    @Override
    public void itematic$setUsedTicks(int usedTicks) {
        this.usedTicks = usedTicks;
    }
}
