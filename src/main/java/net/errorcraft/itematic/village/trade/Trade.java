package net.errorcraft.itematic.village.trade;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.village.TradeOffersAccessor;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record Trade(List<Entry> wants, Entry gives, int maxUses, int tradeExperience, float priceMultiplier) {
    public static final Codec<Trade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItematicCodecs.countRangeList(Entry.CODEC.listOf(), 1, Trade.MAX_WANTED_ENTRIES).fieldOf("wants").forGetter(Trade::wants),
        Entry.CODEC.fieldOf("gives").forGetter(Trade::gives),
        Codec.INT.fieldOf("max_uses").forGetter(Trade::maxUses),
        Codecs.createStrictOptionalFieldCodec(Codec.INT, "trade_experience", 1).forGetter(Trade::tradeExperience),
        Codecs.createStrictOptionalFieldCodec(Codec.FLOAT, "price_multiplier", 0.0f).forGetter(Trade::priceMultiplier)
    ).apply(instance, Trade::new));
    private static final int MAX_WANTED_ENTRIES = 2;

    public TradeOffer createTradeOffer(LootContext context) {
        return new TradeOffer(this.createStack(0, context), this.createStack(1, context), this.gives.createStack(context), this.maxUses, this.tradeExperience, this.priceMultiplier);
    }

    private ItemStack createStack(int index, LootContext context) {
        if (index < 0 || index >= this.wants.size()) {
            return ItemStack.EMPTY;
        }
        return this.wants.get(index).createStack(context);
    }

    public static Builder builder(Entry gives) {
        return new Builder(gives);
    }

    public static Trade of(Entry firstBuy, Entry sell, int maxUses, int tradeExperience) {
        return of(firstBuy, sell, maxUses, tradeExperience, 0.05f);
    }

    public static Trade of(Entry firstBuy, Entry sell, int maxUses, int tradeExperience, float priceMultiplier) {
        return of(List.of(firstBuy), sell, maxUses, tradeExperience, priceMultiplier);
    }

    public static Trade of(List<Entry> wants, Entry gives, int maxUses, int tradeExperience, float priceMultiplier) {
        if (wants.size() > MAX_WANTED_ENTRIES) {
            throw new IllegalArgumentException("Wanted entries must not be more than 2");
        }
        return new Trade(wants, gives, maxUses, tradeExperience, priceMultiplier);
    }

    public static class Builder {
        private final List<Entry> wants = new ArrayList<>();
        private final Entry gives;
        private int maxUses = TradeOffersAccessor.getDefaultMaxUses();
        private int tradeExperience;
        private float priceMultiplier = 0.05f;

        public Builder(Entry gives) {
            this.gives = gives;
        }

        public Trade build() {
            return Trade.of(this.wants, this.gives, this.maxUses, this.tradeExperience, this.priceMultiplier);
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

        public Builder priceMultiplier(float priceMultiplier) {
            this.priceMultiplier = priceMultiplier;
            return this;
        }
    }

    public record Entry(RegistryEntry<Item> item, CountProvider count, Optional<LootFunction> itemModifier) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(Entry::item),
            Codecs.createStrictOptionalFieldCodec(CountProvider.CODEC, "count", CountProvider.of(1)).forGetter(Entry::count),
            Codecs.createStrictOptionalFieldCodec(LootFunctionTypes.CODEC, "item_modifier").forGetter(Entry::itemModifier)
        ).apply(instance, Entry::new));

        public ItemStack createStack(LootContext context) {
            ItemStack stack = new ItemStack(this.item, this.count.get(context.getRandom()));
            return this.itemModifier.map(itemModifier -> {
                context.markActive(LootContext.itemModifier(itemModifier));
                return itemModifier.apply(stack, context);
            }).orElse(stack);
        }

        public static Entry of(RegistryEntry<Item> item, int count) {
            return of(item, count, null);
        }

        public static Entry of(RegistryEntry<Item> item, int count, LootFunction itemModifier) {
            return new Entry(item, CountProvider.of(count), Optional.ofNullable(itemModifier));
        }

        public static Entry ofEmerald(RegistryEntryLookup<Item> items) {
            return ofEmerald(items, 1);
        }

        public static Entry ofEmerald(RegistryEntryLookup<Item> items, int count) {
            return of(items.getOrThrow(ItemKeys.EMERALD), count);
        }

        public static Entry ofEmerald(RegistryEntryLookup<Item> items, int min, int max) {
            return new Entry(items.getOrThrow(ItemKeys.EMERALD), CountProvider.of(min, max), Optional.empty());
        }
    }

    public record CountProvider(int min, int max) {
        public static final Codec<CountProvider> INLINE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("min").forGetter(CountProvider::min),
            Codec.INT.fieldOf("max").forGetter(CountProvider::max)
        ).apply(instance, CountProvider::new));
        public static final Codec<CountProvider> CODEC = Codecs.validate(Codecs.either(Codec.INT, INLINE_CODEC).xmap(either -> either.map(CountProvider::of, Function.identity()), c -> {
            if (c.min == c.max) {
                return Either.left(c.min);
            }
            return Either.right(c);
        }), c -> {
            if (c.max < c.min) {
                return DataResult.error(() -> "Max must be at least min: " + c);
            }
            return DataResult.success(c);
        });

        public int get(Random random) {
            if (this.min == this.max) {
                return this.min;
            }
            return random.nextInt(this.max - this.min + 1) + this.min;
        }

        public static CountProvider of(int value) {
            return new CountProvider(value, value);
        }

        public static CountProvider of(int min, int max) {
            return new CountProvider(min, max);
        }
    }
}
