package net.errorcraft.itematic.access.world.entity;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

public interface EntityAccess {
    @Nullable
    default ItemEntity itematic$spawnAtLocation(ServerLevel level, ResourceKey<Item> item) {
        return null;
    }
}
