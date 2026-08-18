package net.errorcraft.itematic.access.stats;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;

public interface StatTypeAccess<T> {
    default MapCodec<Stat<T>> itematic$codec() {
        throw new AssertionError("Implemented via mixin");
    }
    default boolean itematic$contains(Holder<T> holder) {
        return false;
    }
    default Stat<T> itematic$get(Holder<T> holder) {
        return this.itematic$get(holder, StatFormatter.DEFAULT);
    }
    default Stat<T> itematic$get(Holder<T> holder, StatFormatter formatter) {
        throw new AssertionError("Implemented via mixin");
    }
}
