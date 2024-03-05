package net.errorcraft.itematic.access.client.gui.screen;

import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;

public interface StatsScreenItemStatsListWidgetEntryAccess {
    RegistryEntry<Item> itematic$registryEntry();
    void itematic$setRegistryEntry(RegistryEntry<Item> entry);
}
