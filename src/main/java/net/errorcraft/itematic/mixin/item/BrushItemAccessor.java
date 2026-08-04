package net.errorcraft.itematic.mixin.item;

import net.minecraft.world.item.BrushItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BrushItem.class)
public interface BrushItemAccessor {
    @Accessor("USE_DURATION")
    static int maxBrushTime() {
        throw new AssertionError();
    }
}
