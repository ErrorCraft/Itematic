package net.errorcraft.itematic.access.world.entity.player;

import net.minecraft.world.level.Level;

public interface StackedItemContentsAccess {
    default void itematic$setLevel(Level level) {}
}
