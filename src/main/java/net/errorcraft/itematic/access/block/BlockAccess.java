package net.errorcraft.itematic.access.block;

import net.minecraft.item.ItemPlacementContext;

public interface BlockAccess {
    default ItemPlacementContext itematic$placementContext(ItemPlacementContext context) {
        return context;
    }
}
