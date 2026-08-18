package net.errorcraft.itematic.access.stats;

import net.minecraft.core.Holder;

public interface StatAccess<T> {
    default Holder<T> itematic$entry() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setEntry(Holder<T> entry) {}
}
