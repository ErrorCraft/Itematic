package net.errorcraft.itematic.access.world.entity.npc.villager;

import net.errorcraft.itematic.world.item.trading.Trade;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.Nullable;

public interface VillagerDataAccess {
    @Nullable
    default TagKey<Trade> itematic$tradeTag() {
        return null;
    }
}
