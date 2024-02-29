package net.errorcraft.itematic.village.trade.modifier.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.Range;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.village.trade.modifier.TradeModifier;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierType;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public record SingleEnchantmentTradeModifier(int index, int baseRandomCost, int perLevelRandomCost, int perLevelCost, RegistryEntryList<Enchantment> enchantments, Range.IntegerRange levels) implements TradeModifier<SingleEnchantmentTradeModifier> {
    public static final Codec<SingleEnchantmentTradeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("index").forGetter(SingleEnchantmentTradeModifier::index),
        Codec.INT.fieldOf("base_random_cost").forGetter(SingleEnchantmentTradeModifier::baseRandomCost),
        Codec.INT.fieldOf("per_level_random_cost").forGetter(SingleEnchantmentTradeModifier::perLevelRandomCost),
        Codec.INT.fieldOf("per_level_cost").forGetter(SingleEnchantmentTradeModifier::perLevelCost),
        RegistryCodecs.entryList(RegistryKeys.ENCHANTMENT, true).fieldOf("enchantments").forGetter(SingleEnchantmentTradeModifier::enchantments),
        Range.INT_CODEC.fieldOf("levels").forGetter(SingleEnchantmentTradeModifier::levels)
    ).apply(instance, SingleEnchantmentTradeModifier::new));
    private static final int TREASURE_BONUS_FACTOR = 2;

    @Override
    public TradeModifierType<SingleEnchantmentTradeModifier> type() {
        return TradeModifierTypes.SINGLE_ENCHANTMENT;
    }

    @Override
    public ItemStack apply(Trade.Input wants, ItemStack gives, LootContext context) {
        this.enchantments.getRandom(context.getRandom())
            .ifPresent(entry -> this.apply(wants.get(this.index), gives, context, entry.value()));
        return gives;
    }

    public static SingleEnchantmentTradeModifier of(int index, int baseRandomCost, int perLevelRandomCost, int perLevelCost, RegistryEntryList<Enchantment> enchantments) {
        return new SingleEnchantmentTradeModifier(index, baseRandomCost, perLevelRandomCost, perLevelCost, enchantments, Range.IntegerRange.atLeast(1));
    }

    private void apply(ItemStack wants, ItemStack gives, LootContext context, Enchantment enchantment) {
        Random random = context.getRandom();
        int minLevel = Math.max(enchantment.getMinLevel(), this.levels.min());
        int maxLevel = Math.min(enchantment.getMaxLevel(), this.levels.max());
        int level = MathHelper.nextInt(random, minLevel, maxLevel);

        gives.addEnchantment(enchantment, level);
        int count = random.nextInt(this.baseRandomCost + level * this.perLevelRandomCost) + level * this.perLevelCost;
        if (enchantment.isTreasure()) {
            count *= TREASURE_BONUS_FACTOR;
        }
        wants.itematic$tryIncrement(count);
    }
}
