package net.errorcraft.itematic.access.client.render.model;

import net.minecraft.item.Item;
import net.minecraft.registry.Registry;

public interface BakedModelManagerAccess {
    default void setItemRegistry(Registry<Item> itemRegistry) {}
}
