package net.errorcraft.itematic.world.item.trading.modifier;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.item.trading.modifier.modifiers.EnchantWithLevelsTradeModifier;
import net.errorcraft.itematic.world.item.trading.modifier.modifiers.ItemFromTypeTradeModifier;
import net.errorcraft.itematic.world.item.trading.modifier.modifiers.SingleEnchantmentTradeModifier;
import net.minecraft.core.Registry;

public record TradeModifierType<T extends TradeModifier<T>>(MapCodec<T> codec) {
    public static final TradeModifierType<EnchantWithLevelsTradeModifier> ENCHANT_WITH_LEVELS = register(
        "enchant_with_levels",
        new TradeModifierType<>(EnchantWithLevelsTradeModifier.CODEC)
    );
    public static final TradeModifierType<SingleEnchantmentTradeModifier> SINGLE_ENCHANTMENT = register(
        "single_enchantment",
        new TradeModifierType<>(SingleEnchantmentTradeModifier.CODEC)
    );
    public static final TradeModifierType<ItemFromTypeTradeModifier> ITEM_FROM_TYPE = register(
        "item_from_type",
        new TradeModifierType<>(ItemFromTypeTradeModifier.CODEC)
    );

    public static void init() {}

    private static <T extends TradeModifier<T>> TradeModifierType<T> register(String id, TradeModifierType<T> type) {
        return Registry.register(ItematicBuiltInRegistries.TRADE_MODIFIER_TYPE, id, type);
    }
}
