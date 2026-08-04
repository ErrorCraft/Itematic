package net.errorcraft.itematic.access.stat;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;

public interface StatTypeAccess<T> {
    default MapCodec<Stat<T>> itematic$codec() {
        return null;
    }

    default boolean itematic$hasStat(Holder<T> entry) {
        return false;
    }

    default Stat<T> itematic$getOrCreateStat(Holder<T> entry) {
        return this.itematic$getOrCreateStat(entry, StatFormatter.DEFAULT);
    }

    default Stat<T> itematic$getOrCreateStat(Holder<T> entry, StatFormatter formatter) {
        return null;
    }
}
