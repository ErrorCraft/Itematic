package net.errorcraft.itematic.access.block;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public interface AbstractPlantStemBlockAccess {
    default RegistryKey<Item> itematic$stemItemKey() {
        return ItemKeys.AIR;
    }

    default void itematic$setStemItemKey(RegistryKey<Item> stemItemKey) {}
}
