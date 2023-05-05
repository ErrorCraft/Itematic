package net.errorcraft.itematic.access.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public interface BoatEntityAccess {
    default RegistryKey<Item> asItemKey() {
        return ItemKeys.AIR;
    }
}
