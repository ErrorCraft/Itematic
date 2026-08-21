package net.errorcraft.itematic.access.world.entity;

public interface MobAccess {
    default boolean itematic$trySetBaby(boolean baby) {
        return false;
    }
}
