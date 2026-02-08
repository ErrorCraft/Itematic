package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.BundleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BundleItem.class)
public interface BundleItemAccessor {
    @Accessor("ITEM_BAR_COLOR")
    static int itemBarColor() {
        throw new AssertionError();
    }

    @Accessor("FULL_ITEM_BAR_COLOR")
    static int fullItemBarColor() {
        throw new AssertionError();
    }

    @Accessor("field_54109")
    static int ticksAfterFirstThrow() {
        throw new AssertionError();
    }

    @Accessor("field_54110")
    static int ticksBetweenThrows() {
        throw new AssertionError();
    }

    @Accessor("MAX_USE_TIME")
    static int useDuration() {
        throw new AssertionError();
    }
}
