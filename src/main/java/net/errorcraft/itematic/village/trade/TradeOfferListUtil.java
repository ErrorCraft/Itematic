package net.errorcraft.itematic.village.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;

import java.util.List;

public class TradeOfferListUtil {
    public static final Codec<TradeOfferList> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TradeOfferUtil.CODEC.listOf().fieldOf("Recipes").forGetter(l -> l)
    ).apply(instance, TradeOfferListUtil::create));

    private TradeOfferListUtil() {}

    private static TradeOfferList create(List<TradeOffer> tradeOffers) {
        TradeOfferList list = new TradeOfferList();
        list.addAll(tradeOffers);
        return list;
    }
}
