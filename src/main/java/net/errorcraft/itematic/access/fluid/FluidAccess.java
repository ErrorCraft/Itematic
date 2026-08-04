package net.errorcraft.itematic.access.fluid;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface FluidAccess {
    default ResourceKey<Item> itematic$getBucketItemKey() {
        return null;
    }
}
