package net.errorcraft.itematic.access.client.item;

public interface BundleTooltipDataAccess {
    default int itematic$capacity() {
        return 0;
    }
    default void itematic$setCapacity(int capacity) {}
}
