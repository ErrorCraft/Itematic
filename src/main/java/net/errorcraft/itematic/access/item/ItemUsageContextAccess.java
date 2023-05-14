package net.errorcraft.itematic.access.item;

public interface ItemUsageContextAccess {
    default boolean ignoresPlacementComponent() {
        return false;
    }

    default void setIgnoresPlacementComponent(boolean ignoresPlacementComponent) {}
}
