package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.HoneyBottleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HoneyBottleItem.class)
public interface HoneyBottleItemAccessor {
    @Accessor("MAX_USE_TIME")
    static int maxUseTime() {
        throw new AssertionError();
    }
}
