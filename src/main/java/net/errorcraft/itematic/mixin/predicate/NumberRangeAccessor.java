package net.errorcraft.itematic.mixin.predicate;

import net.minecraft.predicate.NumberRange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

public interface NumberRangeAccessor {
    @Mixin(NumberRange.IntRange.class)
    interface IntRangeAccessor {
        @Invoker("<init>")
        static NumberRange.IntRange create(Optional<Integer> min, Optional<Integer> max) {
            throw new AssertionError();
        }
    }
}
