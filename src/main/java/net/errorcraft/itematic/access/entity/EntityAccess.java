package net.errorcraft.itematic.access.entity;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public interface EntityAccess {
    default ItemEntity itematic$dropItem(RegistryKey<Item> key) {
        return this.itematic$dropItem(key, 0);
    }
    default ItemEntity itematic$dropItem(RegistryKey<Item> key, int yOffset) {
        return null;
    }
    default ItemEntity itematic$dropItem(RegistryEntry<Item> entry) {
        return this.itematic$dropItem(entry, 0);
    }
    default ItemEntity itematic$dropItem(RegistryEntry<Item> entry, int yOffset) {
        return null;
    }
}
