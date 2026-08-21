package net.errorcraft.itematic.mixin.advancements.criterion;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.criterion.MinMaxBounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface MinMaxBoundsAccessor {
    @Mixin(MinMaxBounds.Bounds.class)
    interface BoundsAccessor {
        @Invoker("createCodec")
        static <T extends Number & Comparable<T>> Codec<MinMaxBounds.Bounds<T>> createCodec(Codec<T> numberCodec) {
            throw new AssertionError();
        }
    }
}
