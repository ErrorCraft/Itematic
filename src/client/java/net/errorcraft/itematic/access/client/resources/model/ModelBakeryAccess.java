package net.errorcraft.itematic.access.client.resources.model;

import net.minecraft.client.renderer.item.ItemModel;

public interface ModelBakeryAccess {
    interface BakingResultAccess {
        default ItemModel itematic$failedToLoadItemModel() {
            throw new AssertionError("Implemented via mixin");
        }
        default void itematic$setFailedToLoadItemModel(ItemModel failedToLoadItemModel) {}
    }
}
