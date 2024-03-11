package net.errorcraft.itematic.access.block;

import net.minecraft.component.ComponentMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.RegistryKey;

public interface BlockAccess {
    default RegistryKey<Item> itematic$asItemKey() {
        return null;
    }
    default void itematic$setAsItemKey(RegistryKey<Item> pickBlockKey) {}
    default void itematic$addComponents(ComponentMap.Builder builder) {}
    default ItemPlacementContext itematic$placementContext(ItemPlacementContext context) {
        return context;
    }
}
