package net.errorcraft.itematic.access.loot.entry;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public interface ItemEntryAccess {
    default RegistryKey<Item> getItemKey() {
        return null;
    }
    default void setItemKey(RegistryKey<Item> entry) {}
}
