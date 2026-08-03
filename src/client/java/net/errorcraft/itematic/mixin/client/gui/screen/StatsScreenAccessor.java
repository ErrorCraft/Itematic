package net.errorcraft.itematic.mixin.client.gui.screen;

import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface StatsScreenAccessor {
    interface ItemStatsListWidgetAccessor {
        @Mixin(StatsScreen.ItemStatsListWidget.StatEntry.class)
        interface StatEntryAccessor {
            @Invoker("<init>")
            static StatsScreen.ItemStatsListWidget.StatEntry create(StatsScreen.ItemStatsListWidget widget, Item item) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
