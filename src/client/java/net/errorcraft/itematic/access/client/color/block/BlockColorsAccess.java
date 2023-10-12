package net.errorcraft.itematic.access.client.color.block;

import net.minecraft.item.Item;
import net.minecraft.registry.Registry;

public interface BlockColorsAccess {
    default Registry<Item> itemRegistry() {
        return null;
    }
    default void setItemRegistry(Registry<Item> itemRegistry) {}
}
