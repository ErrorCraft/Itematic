package net.errorcraft.itematic.village.trade.modifier;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class TradeModifierTypeKeys {
    public static final ResourceKey<TradeModifierType<?>> ENCHANT_WITH_LEVELS = of("enchant_with_levels");
    public static final ResourceKey<TradeModifierType<?>> SINGLE_ENCHANTMENT = of("single_enchantment");
    public static final ResourceKey<TradeModifierType<?>> ITEM_FROM_TYPE = of("item_from_type");

    private TradeModifierTypeKeys() {}

    private static ResourceKey<TradeModifierType<?>> of(String key) {
        return ResourceKey.create(ItematicRegistries.TRADE_MODIFIER_TYPE, Identifier.withDefaultNamespace(key));
    }
}
