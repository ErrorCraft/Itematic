package net.errorcraft.itematic.access.entity;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;

public interface EntityAccess {
    default ItemEntity itematic$dropItem(ServerWorld world, RegistryKey<Item> key) {
        return null;
    }
    default ItemEntity itematic$dropItem(ServerWorld world, RegistryKey<Item> key, float yOffset) {
        return null;
    }
    default ItemEntity itematic$dropItem(ServerWorld world, RegistryEntry<Item> entry) {
        return null;
    }
    default ItemEntity itematic$dropItem(ServerWorld world, RegistryEntry<Item> entry, float yOffset) {
        return null;
    }
}
