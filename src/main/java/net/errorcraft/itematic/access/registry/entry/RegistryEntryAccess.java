package net.errorcraft.itematic.access.registry.entry;

import net.minecraft.core.Holder;
import org.jetbrains.annotations.NotNull;

public interface RegistryEntryAccess<T> extends Comparable<Holder<T>> {
    @Override
    default int compareTo(@NotNull Holder<T> o) {
        return 0;
    }

    default int itematic$rawId() {
        return -1;
    }
    default void itematic$setRawId(int id) {}
}
