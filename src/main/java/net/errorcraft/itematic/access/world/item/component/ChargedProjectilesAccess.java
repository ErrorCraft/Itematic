package net.errorcraft.itematic.access.world.item.component;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface ChargedProjectilesAccess {
    default boolean itematic$contains(ResourceKey<Item> item) {
        return false;
    }
}
