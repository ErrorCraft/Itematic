package net.errorcraft.itematic.access.component.type;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface ChargedProjectilesComponentAccess {
    default boolean itematic$contains(ResourceKey<Item> item) {
        return false;
    }
}
