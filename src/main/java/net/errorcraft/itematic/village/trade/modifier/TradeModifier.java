package net.errorcraft.itematic.village.trade.modifier;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.village.trade.Trade;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.village.TradedItem;

import java.util.Optional;

public interface TradeModifier<T extends TradeModifier<T>> {
    Codec<TradeModifier<?>> CODEC = ItematicRegistries.TRADE_MODIFIER_TYPE.getCodec().dispatch(TradeModifier::type, TradeModifierType::codec);

    TradeModifierType<T> type();
    Optional<TradedItem> apply(Trade.Input wants, ItemStack gives, LootContext context);
}
