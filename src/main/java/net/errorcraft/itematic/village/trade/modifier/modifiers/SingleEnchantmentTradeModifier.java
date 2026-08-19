package net.errorcraft.itematic.village.trade.modifier.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.RandomRange;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.village.trade.modifier.TradeModifier;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierType;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.Optional;

public record SingleEnchantmentTradeModifier(int index, int baseRandomCost, int perLevelRandomCost, int perLevelCost, HolderSet<Enchantment> enchantments, RandomRange.Integers levels) implements TradeModifier<SingleEnchantmentTradeModifier> {
    public static final MapCodec<SingleEnchantmentTradeModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Trade.WANTED_INDEX_CODEC.fieldOf("index").forGetter(SingleEnchantmentTradeModifier::index),
        Codec.INT.fieldOf("base_random_cost").forGetter(SingleEnchantmentTradeModifier::baseRandomCost),
        Codec.INT.fieldOf("per_level_random_cost").forGetter(SingleEnchantmentTradeModifier::perLevelRandomCost),
        Codec.INT.fieldOf("per_level_cost").forGetter(SingleEnchantmentTradeModifier::perLevelCost),
        RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).fieldOf("enchantments").forGetter(SingleEnchantmentTradeModifier::enchantments),
        RandomRange.Integers.CODEC.fieldOf("levels").forGetter(SingleEnchantmentTradeModifier::levels)
    ).apply(instance, SingleEnchantmentTradeModifier::new));

    public static SingleEnchantmentTradeModifier of(int index, int baseRandomCost, int perLevelRandomCost, int perLevelCost, HolderSet<Enchantment> enchantments) {
        return new SingleEnchantmentTradeModifier(index, baseRandomCost, perLevelRandomCost, perLevelCost, enchantments, RandomRange.Integers.atLeast(1));
    }

    @Override
    public TradeModifierType<SingleEnchantmentTradeModifier> type() {
        return TradeModifierTypes.SINGLE_ENCHANTMENT;
    }

    @Override
    public Optional<ItemCost> apply(Trade.Input wants, ItemStack gives, LootContext context) {
        RandomSource random = context.getRandom();
        this.enchantments.getRandomElement(random)
            .ifPresent(entry -> this.apply(wants.getStack(this.index), gives, random, entry));
        return Optional.of(new ItemCost(gives.getItemHolder(), gives.getCount(), DataComponentExactPredicate.allOf(gives.getComponents())));
    }

    private void apply(ItemStack wants, ItemStack gives, RandomSource random, Holder<Enchantment> enchantment) {
        int minLevel = Math.max(enchantment.value().getMinLevel(), this.levels.min());
        int maxLevel = Math.min(enchantment.value().getMaxLevel(), this.levels.max());
        int level = Mth.nextInt(random, minLevel, maxLevel);

        gives.enchant(enchantment, level);
        int count = random.nextInt(this.baseRandomCost + level * this.perLevelRandomCost) + level * this.perLevelCost;
        if (enchantment.is(EnchantmentTags.DOUBLE_TRADE_PRICE)) {
            count *= 2;
        }

        wants.itematic$tryIncrement(count);
    }
}
