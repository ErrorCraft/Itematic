package net.errorcraft.itematic.access.world.level.block;

import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface CropBlockAccess {
    default ResourceKey<Item> itematic$seedsItemId() {
        return BlockItemIds.WHEAT_CROP.item();
    }
}
