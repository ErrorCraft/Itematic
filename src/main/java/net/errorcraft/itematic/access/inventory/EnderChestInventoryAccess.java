package net.errorcraft.itematic.access.inventory;

import net.minecraft.entity.player.PlayerEntity;

public interface EnderChestInventoryAccess {
    default void itematic$setPlayer(PlayerEntity player) {}
}
