package net.errorcraft.itematic.access.client.gui.screens.achievement;

import net.minecraft.core.Holder;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.item.Item;

public interface StatsScreenAccess {
    default StatsCounter itematic$stats() {
        throw new AssertionError("Implemented via mixin");
    }

    interface ItemStatisticsListAccess {
        interface ItemRowAccess {
            default Holder<Item> itematic$item() {
                throw new AssertionError("Implemented via mixin");
            }
            default void itematic$setItem(Holder<Item> item) {}
        }
    }
}
