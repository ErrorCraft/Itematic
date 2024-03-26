package net.errorcraft.itematic.village.trade.modifier;

import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.village.trade.modifier.modifiers.EnchantWithLevelsTradeModifier;
import net.errorcraft.itematic.village.trade.modifier.modifiers.ItemFromTypeTradeModifier;
import net.errorcraft.itematic.village.trade.modifier.modifiers.SingleEnchantmentTradeModifier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class TradeModifierTypes {
    public static final TradeModifierType<EnchantWithLevelsTradeModifier> ENCHANT_WITH_LEVELS = register(TradeModifierTypeKeys.ENCHANT_WITH_LEVELS, new TradeModifierType<>(EnchantWithLevelsTradeModifier.CODEC));
    public static final TradeModifierType<SingleEnchantmentTradeModifier> SINGLE_ENCHANTMENT = register(TradeModifierTypeKeys.SINGLE_ENCHANTMENT, new TradeModifierType<>(SingleEnchantmentTradeModifier.CODEC));
    public static final TradeModifierType<ItemFromTypeTradeModifier> ITEM_FROM_TYPE = register(TradeModifierTypeKeys.ITEM_FROM_TYPE, new TradeModifierType<>(ItemFromTypeTradeModifier.CODEC));

    private TradeModifierTypes() {}

    public static void init() {}

    private static <T extends TradeModifier<T>> TradeModifierType<T> register(RegistryKey<TradeModifierType<?>> id, TradeModifierType<T> type) {
        return Registry.register(ItematicRegistries.TRADE_MODIFIER_TYPE, id, type);
    }
}
