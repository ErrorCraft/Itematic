package net.errorcraft.itematic.access.entity;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;

public interface EntityAccess {
    default ItemEntity dropItem(RegistryEntry<Item> entry) {
        return this.dropItem(entry, 0);
    }

    default ItemEntity dropItem(RegistryEntry<Item> entry, int yOffset) {
        return null;
    }
}
