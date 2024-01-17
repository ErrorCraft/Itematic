package net.errorcraft.itematic.access.client.render.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registry;

public interface ItemRendererAccess {
    default void itematic$reloadModelIds(Registry<Item> registry) {}
}
