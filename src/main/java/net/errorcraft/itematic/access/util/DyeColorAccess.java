package net.errorcraft.itematic.access.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface DyeColorAccess {
    default ResourceKey<Item> itematic$itemKey() {
        return null;
    }
    default void itematic$setItemKey(ResourceKey<Item> item) {}
}
