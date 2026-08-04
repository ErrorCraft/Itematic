package net.errorcraft.itematic.access.block;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface AbstractPlantStemBlockAccess {
    default ResourceKey<Item> itematic$stemItemKey() {
        return ItemKeys.AIR;
    }

    default void itematic$setStemItemKey(ResourceKey<Item> stemItemKey) {}
}
