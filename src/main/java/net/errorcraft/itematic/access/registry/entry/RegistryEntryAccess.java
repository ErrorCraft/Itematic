package net.errorcraft.itematic.access.registry.entry;

public interface RegistryEntryAccess {
    default int getRawId() {
        return -1;
    }
    default void setRawId(int id) {}
}
