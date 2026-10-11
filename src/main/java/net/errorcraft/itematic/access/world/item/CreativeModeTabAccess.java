package net.errorcraft.itematic.access.world.item;

public interface CreativeModeTabAccess {
    default boolean itematic$place(int index) {
        return false;
    }
}
