package net.errorcraft.itematic.village.trade.modifier;

import com.mojang.serialization.Codec;

public record TradeModifierType<T extends TradeModifier<T>>(Codec<T> codec) {
}
