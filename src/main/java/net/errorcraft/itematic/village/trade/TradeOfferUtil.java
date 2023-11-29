package net.errorcraft.itematic.village.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.village.TradeOffer;

public class TradeOfferUtil {
    public static final Codec<TradeOffer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.CODEC.fieldOf("buy").forGetter(TradeOffer::getOriginalFirstBuyItem),
        ItemStack.CODEC.fieldOf("buyB").forGetter(TradeOffer::getSecondBuyItem),
        ItemStack.CODEC.fieldOf("sell").forGetter(TradeOffer::getSellItem),
        Codec.INT.fieldOf("uses").forGetter(TradeOffer::getUses),
        Codecs.createStrictOptionalFieldCodec(Codec.INT, "maxUses", 4).forGetter(TradeOffer::getMaxUses),
        Codecs.createStrictOptionalFieldCodec(Codec.INT, "xp", 1).forGetter(TradeOffer::getMerchantExperience),
        Codecs.createStrictOptionalFieldCodec(Codec.FLOAT, "priceMultiplier", 0.0f).forGetter(TradeOffer::getPriceMultiplier),
        Codec.INT.fieldOf("demand").forGetter(TradeOffer::getDemandBonus),
        Codec.INT.fieldOf("specialPrice").forGetter(TradeOffer::getSpecialPrice),
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "rewardExp", true).forGetter(TradeOffer::shouldRewardPlayerExperience)
    ).apply(instance, TradeOfferUtil::create));

    private TradeOfferUtil() {}

    private static TradeOffer create(ItemStack firstBuyStack, ItemStack secondBuyItem, ItemStack sellStack, int uses, int maxUses, int tradeExperience, float priceMultiplier, int demandBonus, int specialPrice, boolean rewardExperience) {
        TradeOffer tradeOffer = new TradeOffer(firstBuyStack, secondBuyItem, sellStack, uses, maxUses, tradeExperience, priceMultiplier, demandBonus);
        tradeOffer.setSpecialPrice(specialPrice);
        tradeOffer.itematic$rewardsPlayerExperience(rewardExperience);
        return tradeOffer;
    }
}
