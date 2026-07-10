package net.errorcraft.itematic.mixin.predicate;

import com.mojang.serialization.Codec;
import net.minecraft.predicate.NumberRange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface NumberRangeAccessor {
    @Mixin(NumberRange.Bounds.class)
    interface BoundsAccessor {
        @Invoker("createCodec")
        static <T extends Number & Comparable<T>> Codec<NumberRange.Bounds<T>> createCodec(Codec<T> valueCodec) {
            throw new AssertionError();
        }
    }
}
