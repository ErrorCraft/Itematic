package net.errorcraft.itematic.access.util;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public interface DyeColorAccess {
    default RegistryKey<Item> itematic$itemKey() {
        return null;
    }
    default void itematic$setItemKey(RegistryKey<Item> item) {}
}
