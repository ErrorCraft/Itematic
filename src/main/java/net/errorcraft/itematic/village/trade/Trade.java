package net.errorcraft.itematic.village.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.village.TradeOffersAccessor;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.util.Range;
import net.errorcraft.itematic.village.trade.modifier.TradeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.predicate.ComponentPredicate;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Trade(List<Entry> wants, Entry gives, int maxUses, int tradeExperience, float priceMultiplier, Optional<TradeModifier<?>> tradeModifier) {
    public static final Codec<Trade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItematicCodecs.countRangeList(Entry.CODEC.listOf(), 1, Trade.MAX_WANTED_ENTRIES).fieldOf("wants").forGetter(Trade::wants),
        Entry.CODEC.fieldOf("gives").forGetter(Trade::gives),
        Codec.INT.fieldOf("max_uses").forGetter(Trade::maxUses),
        Codecs.createStrictOptionalFieldCodec(Codec.INT, "trade_experience", 1).forGetter(Trade::tradeExperience),
        Codecs.createStrictOptionalFieldCodec(Codec.FLOAT, "price_multiplier", 0.0f).forGetter(Trade::priceMultiplier),
        Codecs.createStrictOptionalFieldCodec(TradeModifier.CODEC, "trade_modifier").forGetter(Trade::tradeModifier)
    ).apply(instance, Trade::new));
    private static final int MAX_WANTED_ENTRIES = 2;

    public TradeOffer createTradeOffer(LootContext context) {
        Input wants = this.createWantedStacks(context);
        TradedItem gives = this.createGivenStack(wants, context);
        return new TradeOffer(wants.getTradedItem(0).orElseThrow(), wants.getTradedItem(1), gives.itemStack(), this.maxUses, this.tradeExperience, this.priceMultiplier);
    }

    private Input createWantedStacks(LootContext context) {
        List<ItemStack> stacks = this.wants.stream().map(entry -> entry.createStack(context)).toList();
        return new Input(stacks);
    }

    private TradedItem createGivenStack(Input wants, LootContext context) {
        ItemStack gives = this.gives.createStack(context);
        return this.tradeModifier.flatMap(tradeModifier -> tradeModifier.apply(wants, gives, context))
            .orElseGet(() -> new TradedItem(gives.getRegistryEntry(), gives.getCount(), ComponentPredicate.of(gives.getComponents())));
    }

    public static Builder builder(Entry gives) {
        return new Builder(gives);
    }

    public static Trade of(Entry firstBuy, Entry sell, int maxUses, int tradeExperience) {
        return of(firstBuy, sell, maxUses, tradeExperience, 0.05f);
    }

    public static Trade of(Entry firstBuy, Entry sell, int maxUses, int tradeExperience, float priceMultiplier) {
        return of(List.of(firstBuy), sell, maxUses, tradeExperience, priceMultiplier, null);
    }

    public static Trade of(List<Entry> wants, Entry gives, int maxUses, int tradeExperience, float priceMultiplier, TradeModifier<?> tradeModifier) {
        if (wants.size() > MAX_WANTED_ENTRIES) {
            throw new IllegalArgumentException("Wanted entries must not be more than " + MAX_WANTED_ENTRIES);
        }
        return new Trade(wants, gives, maxUses, tradeExperience, priceMultiplier, Optional.ofNullable(tradeModifier));
    }

    public static class Builder {
        private final List<Entry> wants = new ArrayList<>();
        private final Entry gives;
        private int maxUses = TradeOffersAccessor.defaultMaxUses();
        private int tradeExperience;
        private float priceMultiplier = TradeOffersAccessor.lowPriceMultiplier();
        private TradeModifier<?> tradeModifier;

        public Builder(Entry gives) {
            this.gives = gives;
        }

        public Trade build() {
            return Trade.of(this.wants, this.gives, this.maxUses, this.tradeExperience, this.priceMultiplier, this.tradeModifier);
        }

        public Builder wants(Entry entry) {
            if (this.wants.size() >= MAX_WANTED_ENTRIES) {
                throw new IllegalArgumentException("Tried to add more than " + MAX_WANTED_ENTRIES + " wanted entries");
            }
            this.wants.add(entry);
            return this;
        }

        public Builder maxUses(int maxUses) {
            this.maxUses = maxUses;
            return this;
        }

        public Builder tradeExperience(int tradeExperience) {
            this.tradeExperience = tradeExperience;
            return this;
        }

        public Builder tradeModifier(TradeModifier<?> tradeModifier) {
            this.tradeModifier = tradeModifier;
            return this;
        }

        public Builder priceMultiplier(float priceMultiplier) {
            this.priceMultiplier = priceMultiplier;
            return this;
        }
    }

    public static class Input {
        private final List<ItemStack> stacks;

        private Input(List<ItemStack> stacks) {
            this.stacks = stacks;
        }

        public Optional<TradedItem> getTradedItem(int index) {
            if (index < 0 || index >= this.stacks.size()) {
                return Optional.empty();
            }
            ItemStack stack = this.stacks.get(index);
            return Optional.of(new TradedItem(stack.getRegistryEntry(), stack.getCount(), ComponentPredicate.of(stack.getComponents())));
        }

        public ItemStack getStack(int index) {
            if (index < 0 || index >= this.stacks.size()) {
                return ItemStack.EMPTY;
            }
            return this.stacks.get(index);
        }
    }

    public record Entry(RegistryEntry<Item> item, Range.IntegerRange count, Optional<LootFunction> itemModifier) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(Entry::item),
            Codecs.createStrictOptionalFieldCodec(Range.INT_CODEC, "count", Range.IntegerRange.of(1)).forGetter(Entry::count),
            Codecs.createStrictOptionalFieldCodec(LootFunctionTypes.CODEC, "item_modifier").forGetter(Entry::itemModifier)
        ).apply(instance, Entry::new));

        public ItemStack createStack(LootContext context) {
            ItemStack stack = new ItemStack(this.item, this.count.get(context.getRandom()));
            return this.itemModifier.map(itemModifier -> {
                context.markActive(LootContext.itemModifier(itemModifier));
                return itemModifier.apply(stack, context);
            }).orElse(stack);
        }

        public static Entry of(RegistryEntry<Item> item) {
            return of(item, 1, null);
        }

        public static Entry of(RegistryEntry<Item> item, int count) {
            return of(item, count, null);
        }

        public static Entry of(RegistryEntry<Item> item, int count, LootFunction itemModifier) {
            return new Entry(item, Range.IntegerRange.of(count), Optional.ofNullable(itemModifier));
        }

        public static Entry ofEmerald(RegistryEntryLookup<Item> items) {
            return ofEmerald(items, 1);
        }

        public static Entry ofEmerald(RegistryEntryLookup<Item> items, int count) {
            return of(items.getOrThrow(ItemKeys.EMERALD), count);
        }
    }
}
