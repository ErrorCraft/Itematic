package net.errorcraft.itematic.access.client.render.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registry;

public interface ItemModelsAccess {
    default void itematic$reloadModelIds(Registry<Item> registry) {}
}
