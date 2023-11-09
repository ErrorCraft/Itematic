package net.errorcraft.itematic.access.registry.entry;

public interface RegistryEntryAccess {
    default int itematic$rawId() {
        return -1;
    }
    default void itematic$setRawId(int id) {}
}
