package net.errorcraft.itematic.access.world.phys.shapes;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface CollisionContextAccess {
    default boolean itematic$isHoldingItem(ResourceKey<Item> item) {
        return false;
    }
}
