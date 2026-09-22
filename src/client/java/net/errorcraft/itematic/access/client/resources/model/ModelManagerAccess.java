package net.errorcraft.itematic.access.client.resources.model;

import net.minecraft.client.renderer.item.ItemModel;

public interface ModelManagerAccess {
    default ItemModel itematic$failedToLoadItemModel() {
        throw new AssertionError("Implemented via mixin");
    }
}
