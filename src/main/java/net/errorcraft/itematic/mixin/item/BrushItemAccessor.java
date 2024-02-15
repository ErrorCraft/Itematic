package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.BrushItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BrushItem.class)
public interface BrushItemAccessor {
    @Accessor("MAX_BRUSH_TIME")
    static int maxBrushTime() {
        throw new AssertionError();
    }
}
