package net.errorcraft.itematic.access.stat;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;

public interface StatTypeAccess<T> {
    default boolean itematic$hasStat(RegistryEntry<T> entry) {
        return false;
    }

    default Stat<T> itematic$getOrCreateStat(RegistryEntry<T> entry) {
        return this.itematic$getOrCreateStat(entry, StatFormatter.DEFAULT);
    }

    default Stat<T> itematic$getOrCreateStat(RegistryEntry<T> entry, StatFormatter formatter) {
        return null;
    }
}
