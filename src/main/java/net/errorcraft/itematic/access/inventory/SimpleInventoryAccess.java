package net.errorcraft.itematic.access.inventory;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public interface SimpleInventoryAccess {
    void itematic$removeItem(RegistryKey<Item> item, int count);
}
