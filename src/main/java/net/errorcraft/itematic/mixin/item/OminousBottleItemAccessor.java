package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.OminousBottleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OminousBottleItem.class)
public interface OminousBottleItemAccessor {
    @Accessor("MAX_USE_TIME")
    static int useDuration() {
        throw new AssertionError();
    }
}
