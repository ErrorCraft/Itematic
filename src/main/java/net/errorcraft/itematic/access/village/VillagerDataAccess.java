package net.errorcraft.itematic.access.village;

import net.errorcraft.itematic.village.trade.Trade;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

public interface VillagerDataAccess {
    @Nullable
    default TagKey<Trade> itematic$tradeTag() {
        return null;
    }
}
