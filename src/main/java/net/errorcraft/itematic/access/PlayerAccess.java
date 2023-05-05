package net.errorcraft.itematic.access;

import net.minecraft.entity.player.PlayerEntity;

public interface PlayerAccess {
    default void setPlayer(PlayerEntity player) {}
}
