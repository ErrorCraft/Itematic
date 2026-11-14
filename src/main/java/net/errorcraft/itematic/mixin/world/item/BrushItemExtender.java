package net.errorcraft.itematic.mixin.world.item;

import net.errorcraft.itematic.world.item.component.BlockBrusher;
import net.minecraft.world.item.BrushItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BrushItem.class)
public class BrushItemExtender {
    @ModifyVariable(
        method = "onUseTick",
        at = @At("LOAD"),
        name = "timeElapsed"
    )
    private int useUsedTicksDirectlyInsteadOfCalculating(int timeElapsed) {
        return BlockBrusher.USED_TICKS.get() + 1;
    }
}
