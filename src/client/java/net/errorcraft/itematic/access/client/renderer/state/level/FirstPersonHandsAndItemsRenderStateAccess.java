package net.errorcraft.itematic.access.client.renderer.state.level;

public interface FirstPersonHandsAndItemsRenderStateAccess {
    default int itematic$usedItemTicks() {
        return 0;
    }
    default void itematic$setUsedItemTicks(int usedTicks) {}
}
