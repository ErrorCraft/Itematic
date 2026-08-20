package net.errorcraft.itematic.mixin.client.gui.screens.achievement;

import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface StatsScreenAccessor {
    interface ItemStatisticsListAccessor {
        @Mixin(StatsScreen.ItemStatisticsList.ItemRow.class)
        interface ItemRowAccessor {
            @Invoker("<init>")
            static StatsScreen.ItemStatisticsList.ItemRow create(StatsScreen.ItemStatisticsList itemStatisticsList, @Nullable Item item) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
