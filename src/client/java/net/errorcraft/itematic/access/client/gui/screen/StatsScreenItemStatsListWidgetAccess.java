package net.errorcraft.itematic.access.client.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;

public interface StatsScreenItemStatsListWidgetAccess {
    default void itematic$renderStatItem(DrawContext context, int x, int y, RegistryEntry<Item> entry) {}
}
