package net.errorcraft.itematic.village.trade.modifier;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class TradeModifierTypeKeys {
    public static final RegistryKey<TradeModifierType<?>> ENCHANT_WITH_LEVELS = of("enchant_with_levels");
    public static final RegistryKey<TradeModifierType<?>> SINGLE_ENCHANTMENT = of("single_enchantment");
    public static final RegistryKey<TradeModifierType<?>> ITEM_FROM_TYPE = of("item_from_type");

    private TradeModifierTypeKeys() {}

    private static RegistryKey<TradeModifierType<?>> of(String key) {
        return RegistryKey.of(ItematicRegistryKeys.TRADE_MODIFIER_TYPE, Identifier.ofVanilla(key));
    }
}
