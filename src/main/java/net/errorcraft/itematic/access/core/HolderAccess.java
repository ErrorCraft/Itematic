package net.errorcraft.itematic.access.core;

import net.minecraft.core.Holder;

public interface HolderAccess<T> extends Comparable<Holder<T>> {
    @Override
    default int compareTo(Holder<T> o) {
        return 0;
    }
    default int itematic$rawId() {
        return -1;
    }
    default void itematic$setRawId(int id) {}
}
