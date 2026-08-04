package net.errorcraft.itematic.mixin.predicate;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.criterion.MinMaxBounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface NumberRangeAccessor {
    @Mixin(MinMaxBounds.Bounds.class)
    interface BoundsAccessor {
        @Invoker("createCodec")
        static <T extends Number & Comparable<T>> Codec<MinMaxBounds.Bounds<T>> createCodec(Codec<T> valueCodec) {
            throw new AssertionError();
        }
    }
}
