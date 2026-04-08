package net.errorcraft.itematic.village.trade.modifier.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.Range;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.village.trade.modifier.TradeModifier;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierType;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.component.ComponentMapPredicate;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradedItem;

import java.util.Optional;

public record EnchantWithLevelsTradeModifier(int index, Range.IntegerRange level, boolean treasure) implements TradeModifier<EnchantWithLevelsTradeModifier> {
    public static final MapCodec<EnchantWithLevelsTradeModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Trade.WANTED_INDEX_CODEC.fieldOf("index").forGetter(EnchantWithLevelsTradeModifier::index),
        Range.INT_CODEC.fieldOf("level").forGetter(EnchantWithLevelsTradeModifier::level),
        Codec.BOOL.optionalFieldOf("treasure", false).forGetter(EnchantWithLevelsTradeModifier::treasure)
    ).apply(instance, EnchantWithLevelsTradeModifier::new));

    public static EnchantWithLevelsTradeModifier of(int index, int minLevel, int maxLevel) {
        return new EnchantWithLevelsTradeModifier(index, Range.IntegerRange.of(minLevel, maxLevel), false);
    }

    @Override
    public TradeModifierType<EnchantWithLevelsTradeModifier> type() {
        return TradeModifierTypes.ENCHANT_WITH_LEVELS;
    }

    @Override
    public Optional<TradedItem> apply(Trade.Input wants, ItemStack gives, LootContext context) {
        Random random = context.getRandom();
        int level = Math.max(1, this.level.get(random));
        DynamicRegistryManager registryManager = context.getWorld().getRegistryManager();
        Optional<RegistryEntryList.Named<Enchantment>> enchantments = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT)
            .getOptional(EnchantmentTags.ON_TRADED_EQUIPMENT);
        ItemStack givesActual = EnchantmentHelper.enchant(random, gives, level, registryManager, enchantments);
        wants.getStack(this.index).itematic$tryIncrement(level);
        return Optional.of(new TradedItem(givesActual.getRegistryEntry(), givesActual.getCount(), ComponentMapPredicate.of(givesActual.getComponents())));
    }
}
