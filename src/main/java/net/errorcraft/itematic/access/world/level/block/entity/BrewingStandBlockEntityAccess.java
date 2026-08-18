package net.errorcraft.itematic.access.world.level.block.entity;

public interface BrewingStandBlockEntityAccess {
    default int itematic$maxBrewingTime() {
        return 0;
    }
    default void itematic$setMaxBrewingTime(int maxBrewingTime) {}
}
