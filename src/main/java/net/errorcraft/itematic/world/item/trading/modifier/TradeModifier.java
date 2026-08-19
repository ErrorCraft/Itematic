package net.errorcraft.itematic.world.item.trading.modifier;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.item.trading.Trade;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.Optional;

public interface TradeModifier<T extends TradeModifier<T>> {
    Codec<TradeModifier<?>> CODEC = ItematicBuiltInRegistries.TRADE_MODIFIER_TYPE.byNameCodec().dispatch(TradeModifier::type, TradeModifierType::codec);

    TradeModifierType<T> type();
    Optional<ItemCost> apply(Trade.Input wants, ItemStack gives, LootContext context);
}
