package net.errorcraft.itematic.access.world.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface DyeColorAccess {
    default ResourceKey<Item> itematic$itemId() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setItemId(ResourceKey<Item> item) {}
}
