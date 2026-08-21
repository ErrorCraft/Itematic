package net.errorcraft.itematic.access.world.level.block;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface CropBlockAccess {
    default ResourceKey<Item> itematic$seedsItemId() {
        return ItemIds.WHEAT_SEEDS;
    }
}
