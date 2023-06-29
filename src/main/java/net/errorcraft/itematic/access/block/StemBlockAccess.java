package net.errorcraft.itematic.access.block;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public interface StemBlockAccess {
    default void setPickBlockItemKey(RegistryKey<Item> key) {}
}
