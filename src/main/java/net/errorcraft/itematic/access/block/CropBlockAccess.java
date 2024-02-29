package net.errorcraft.itematic.access.block;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public interface CropBlockAccess {
    default RegistryKey<Item> itematic$seedsItemKey() {
        return ItemKeys.WHEAT_SEEDS;
    }
}
