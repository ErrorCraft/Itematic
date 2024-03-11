package net.errorcraft.itematic.access.component.type;

public interface BundleContentsComponentAccess {
    default double itematic$occupancy() {
        return 0.0d;
    }
    default void itematic$setOccupancy(double occupancy) {}
}
