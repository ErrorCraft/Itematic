package net.errorcraft.itematic.access.entity.mob;

public interface MobEntityAccess {
    default boolean itematic$trySetBaby(boolean baby) {
        return false;
    }
}
