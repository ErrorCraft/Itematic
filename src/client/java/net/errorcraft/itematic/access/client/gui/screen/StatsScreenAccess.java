package net.errorcraft.itematic.access.client.gui.screen;

import net.minecraft.core.Holder;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.item.Item;

public interface StatsScreenAccess {
    default StatsCounter itematic$statHandler() {
        return null;
    }

    interface ItemStatsListWidgetAccess {
        interface StatEntryAccess {
            default Holder<Item> itematic$registryEntry() {
                return null;
            }
            default void itematic$setRegistryEntry(Holder<Item> entry) {}
        }
    }
}
