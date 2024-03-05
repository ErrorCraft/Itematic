package net.errorcraft.itematic.mixin.client.gui.screen;

import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface StatsScreenAccessor {
    interface ItemStatsListWidgetAccessor {
        @Mixin(StatsScreen.ItemStatsListWidget.Entry.class)
        interface EntryAccessor {
            @Invoker("<init>")
            static StatsScreen.ItemStatsListWidget.Entry create(StatsScreen.ItemStatsListWidget widget, Item item) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
