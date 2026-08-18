package net.errorcraft.itematic.mixin.world.entity.npc.villager;

import net.minecraft.world.entity.npc.villager.VillagerTrades;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VillagerTrades.class)
public interface VillagerTradesAccessor {
    @Accessor("DEFAULT_SUPPLY")
    static int defaultMaxUses() {
        throw new AssertionError();
    }

    @Accessor("COMMON_ITEMS_SUPPLY")
    static int commonMaxUses() {
        throw new AssertionError();
    }

    @Accessor("UNCOMMON_ITEMS_SUPPLY")
    static int rareMaxUses() {
        throw new AssertionError();
    }

    @Accessor("XP_LEVEL_1_SELL")
    static int noviceSellTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("XP_LEVEL_1_BUY")
    static int noviceBuyTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("XP_LEVEL_2_SELL")
    static int apprenticeSellTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("XP_LEVEL_2_BUY")
    static int apprenticeBuyTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("XP_LEVEL_3_SELL")
    static int journeymanSellTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("XP_LEVEL_3_BUY")
    static int journeymanBuyTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("XP_LEVEL_4_SELL")
    static int expertSellTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("XP_LEVEL_4_BUY")
    static int expertBuyTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("XP_LEVEL_5_TRADE")
    static int masterTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("LOW_TIER_PRICE_MULTIPLIER")
    static float lowPriceMultiplier() {
        throw new AssertionError();
    }

    @Accessor("HIGH_TIER_PRICE_MULTIPLIER")
    static float highPriceMultiplier() {
        throw new AssertionError();
    }
}
