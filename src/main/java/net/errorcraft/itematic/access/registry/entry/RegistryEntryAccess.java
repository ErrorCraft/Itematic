package net.errorcraft.itematic.access.registry.entry;

import net.minecraft.registry.entry.RegistryEntry;

public interface RegistryEntryAccess<T> extends Comparable<RegistryEntry<T>> {
    @Override
    default int compareTo(RegistryEntry<T> o) {
        return 0;
    }

    default int itematic$rawId() {
        return -1;
    }
    default void itematic$setRawId(int id) {}
}
