package net.errorcraft.itematic.mixin.client.gui.screen;

import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface StatsScreenAccessor {
    interface ItemStatsListWidgetAccessor {
        @Mixin(StatsScreen.ItemStatisticsList.ItemRow.class)
        interface StatEntryAccessor {
            @Invoker("<init>")
            static StatsScreen.ItemStatisticsList.ItemRow create(StatsScreen.ItemStatisticsList widget, Item item) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
