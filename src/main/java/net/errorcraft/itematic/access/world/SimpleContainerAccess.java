package net.errorcraft.itematic.access.world;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface SimpleContainerAccess {
    default void itematic$removeItem(ResourceKey<Item> item, int count) {}
}
