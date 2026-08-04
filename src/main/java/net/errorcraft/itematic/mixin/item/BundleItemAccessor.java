package net.errorcraft.itematic.mixin.item;

import net.minecraft.world.item.BundleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BundleItem.class)
public interface BundleItemAccessor {
    @Accessor("BAR_COLOR")
    static int itemBarColor() {
        throw new AssertionError();
    }

    @Accessor("FULL_BAR_COLOR")
    static int fullItemBarColor() {
        throw new AssertionError();
    }

    @Accessor("TICKS_AFTER_FIRST_THROW")
    static int ticksAfterFirstThrow() {
        throw new AssertionError();
    }

    @Accessor("TICKS_BETWEEN_THROWS")
    static int ticksBetweenThrows() {
        throw new AssertionError();
    }

    @Accessor("TICKS_MAX_THROW_DURATION")
    static int useDuration() {
        throw new AssertionError();
    }
}
