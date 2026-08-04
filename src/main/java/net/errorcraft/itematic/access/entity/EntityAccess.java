package net.errorcraft.itematic.access.entity;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;

public interface EntityAccess {
    default ItemEntity itematic$dropItem(ServerLevel world, ResourceKey<Item> key) {
        return null;
    }
    default ItemEntity itematic$dropItem(ServerLevel world, ResourceKey<Item> key, float yOffset) {
        return null;
    }
    default ItemEntity itematic$dropItem(ServerLevel world, Holder<Item> entry) {
        return null;
    }
    default ItemEntity itematic$dropItem(ServerLevel world, Holder<Item> entry, float yOffset) {
        return null;
    }
}
