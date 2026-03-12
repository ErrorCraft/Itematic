package net.errorcraft.itematic.mixin.village;

import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TradeOffers.class)
public interface TradeOffersAccessor {
    @Accessor("DEFAULT_MAX_USES")
    static int defaultMaxUses() {
        throw new AssertionError();
    }

    @Accessor("COMMON_MAX_USES")
    static int commonMaxUses() {
        throw new AssertionError();
    }

    @Accessor("RARE_MAX_USES")
    static int rareMaxUses() {
        throw new AssertionError();
    }

    @Accessor("NOVICE_SELL_EXPERIENCE")
    static int noviceSellTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("NOVICE_BUY_EXPERIENCE")
    static int noviceBuyTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("APPRENTICE_SELL_EXPERIENCE")
    static int apprenticeSellTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("APPRENTICE_BUY_EXPERIENCE")
    static int apprenticeBuyTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("JOURNEYMAN_SELL_EXPERIENCE")
    static int journeymanSellTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("JOURNEYMAN_BUY_EXPERIENCE")
    static int journeymanBuyTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("EXPERT_SELL_EXPERIENCE")
    static int expertSellTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("EXPERT_BUY_EXPERIENCE")
    static int expertBuyTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("MASTER_TRADE_EXPERIENCE")
    static int masterTradeExperience() {
        throw new AssertionError();
    }

    @Accessor("LOW_PRICE_MULTIPLIER")
    static float lowPriceMultiplier() {
        throw new AssertionError();
    }

    @Accessor("HIGH_PRICE_MULTIPLIER")
    static float highPriceMultiplier() {
        throw new AssertionError();
    }
}
