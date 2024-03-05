package net.errorcraft.itematic.access.stat;

import net.minecraft.registry.entry.RegistryEntry;

public interface StatAccess<T> {
    default RegistryEntry<T> itematic$entry() {
        return null;
    }
    default void itematic$setEntry(RegistryEntry<T> entry) {}
}
