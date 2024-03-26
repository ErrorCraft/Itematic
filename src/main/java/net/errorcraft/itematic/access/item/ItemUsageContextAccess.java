package net.errorcraft.itematic.access.item;

public interface ItemUsageContextAccess {
    default boolean itematic$ignoresPlacementComponent() {
        return false;
    }

    default void itematic$setIgnoresPlacementComponent(boolean ignoresPlacementComponent) {}
}
