package net.errorcraft.itematic.village.trade.modifier.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.Range;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.village.trade.modifier.TradeModifier;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierType;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.ComponentPredicate;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradedItem;

import java.util.Optional;

public record EnchantWithLevelsTradeModifier(int index, Range.IntegerRange level, boolean treasure) implements TradeModifier<EnchantWithLevelsTradeModifier> {
    public static final Codec<EnchantWithLevelsTradeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("index").forGetter(EnchantWithLevelsTradeModifier::index),
        Range.INT_CODEC.fieldOf("level").forGetter(EnchantWithLevelsTradeModifier::level),
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "treasure", false).forGetter(EnchantWithLevelsTradeModifier::treasure)
    ).apply(instance, EnchantWithLevelsTradeModifier::new));

    @Override
    public TradeModifierType<EnchantWithLevelsTradeModifier> type() {
        return TradeModifierTypes.ENCHANT_WITH_LEVELS;
    }

    @Override
    public Optional<TradedItem> apply(Trade.Input wants, ItemStack gives, LootContext context) {
        Random random = context.getRandom();
        int level = this.level.get(random);
        ItemStack givesActual = EnchantmentHelper.enchant(random, gives, level, this.treasure);
        wants.getStack(this.index).itematic$tryIncrement(level);
        return Optional.of(new TradedItem(givesActual.getRegistryEntry(), givesActual.getCount(), ComponentPredicate.of(givesActual.getComponents())));
    }

    public static EnchantWithLevelsTradeModifier of(int index, int minLevel, int maxLevel) {
        return new EnchantWithLevelsTradeModifier(index, Range.IntegerRange.of(minLevel, maxLevel), false);
    }
}
