package net.errorcraft.itematic.mixin.stats;

import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.StatType;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Stat.class)
public interface StatAccessor {
    @Invoker("<init>")
    static <T> Stat<T> create(StatType<T> type, @Nullable T value, StatFormatter formatter) {
        throw new AssertionError();
    }

    @Invoker("locationToKey")
    static String locationToKey(@Nullable Identifier id) {
        throw new AssertionError();
    }
}
