package net.errorcraft.itematic.village.trade.modifier;

import com.mojang.serialization.MapCodec;

public record TradeModifierType<T extends TradeModifier<T>>(MapCodec<T> codec) {
}
