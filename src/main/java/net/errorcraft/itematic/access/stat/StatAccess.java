package net.errorcraft.itematic.access.stat;

import net.minecraft.core.Holder;

public interface StatAccess<T> {
    default Holder<T> itematic$entry() {
        return null;
    }
    default void itematic$setEntry(Holder<T> entry) {}
}
