package net.errorcraft.itematic.access.block;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface CropBlockAccess {
    default ResourceKey<Item> itematic$seedsItemKey() {
        return ItemKeys.WHEAT_SEEDS;
    }
}
