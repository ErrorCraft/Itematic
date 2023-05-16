package net.errorcraft.itematic.access.entity.mob;

public interface MobEntityAccess {
    default boolean trySetBaby(boolean baby) {
        return false;
    }
}
