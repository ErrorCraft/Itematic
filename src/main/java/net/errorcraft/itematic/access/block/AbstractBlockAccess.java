package net.errorcraft.itematic.access.block;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;

public interface AbstractBlockAccess {
    default ResourceKey<Item> itematic$asItemKey() {
        return null;
    }
    default void itematic$setAsItemKey(ResourceKey<Item> pickBlockKey) {}
    default void itematic$addComponents(DataComponentMap.Builder builder) {}
    default BlockPlaceContext itematic$placementContext(BlockPlaceContext context) {
        return context;
    }
}
