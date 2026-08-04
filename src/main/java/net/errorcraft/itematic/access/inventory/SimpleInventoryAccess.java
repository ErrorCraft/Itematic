package net.errorcraft.itematic.access.inventory;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface SimpleInventoryAccess {
    void itematic$removeItem(ResourceKey<Item> item, int count);
}
