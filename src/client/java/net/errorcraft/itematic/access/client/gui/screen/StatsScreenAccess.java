package net.errorcraft.itematic.access.client.gui.screen;

import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.stat.StatHandler;

public interface StatsScreenAccess {
    default StatHandler itematic$statHandler() {
        return null;
    }

    interface ItemStatsListWidgetAccess {
        interface EntryAccess {
            default RegistryEntry<Item> itematic$registryEntry() {
                return null;
            }
            default void itematic$setRegistryEntry(RegistryEntry<Item> entry) {}
        }
    }
}
