package net.errorcraft.itematic.access.registry.entry;

import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;

public interface RegistryEntryAccess<T> extends Comparable<RegistryEntry<T>> {
    @Override
    default int compareTo(@NotNull RegistryEntry<T> o) {
        return 0;
    }

    default int itematic$rawId() {
        return -1;
    }
    default void itematic$setRawId(int id) {}
}
