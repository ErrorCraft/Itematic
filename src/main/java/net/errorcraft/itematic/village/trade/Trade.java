package net.errorcraft.itematic.village.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.world.entity.npc.villager.VillagerTradesAccessor;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.errorcraft.itematic.util.RandomRange;
import net.errorcraft.itematic.village.trade.modifier.TradeModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Trade(List<Entry> wants, Entry gives, int maxUses, int tradeExperience, float priceMultiplier, Optional<TradeModifier<?>> tradeModifier, Optional<LootItemCondition> merchantPredicate) {
    public static final Codec<Trade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Entry.CODEC.listOf(1, Trade.MAX_WANTED_ENTRIES).fieldOf("wants").forGetter(Trade::wants),
        Entry.CODEC.fieldOf("gives").forGetter(Trade::gives),
        ExtraCodecs.POSITIVE_INT.fieldOf("max_uses").forGetter(Trade::maxUses),
        Codec.INT.optionalFieldOf("trade_experience", 1).forGetter(Trade::tradeExperience),
        Codec.FLOAT.optionalFieldOf("price_multiplier", 0.0f).forGetter(Trade::priceMultiplier),
        TradeModifier.CODEC.optionalFieldOf("trade_modifier").forGetter(Trade::tradeModifier),
        LootItemCondition.DIRECT_CODEC.optionalFieldOf("merchant_predicate").forGetter(Trade::merchantPredicate)
    ).apply(instance, Trade::new));
    public static final Codec<Integer> WANTED_INDEX_CODEC = ItematicCodecs.index(Trade.MAX_WANTED_ENTRIES);
    private static final int MAX_WANTED_ENTRIES = 2;

    @Nullable
    public MerchantOffer createTradeOffer(LootContext context) {
        if (!this.test(context)) {
            return null;
        }

        Input wants = this.createWantedStacks(context);
        ItemCost gives = this.createGivenStack(wants, context);
        return new MerchantOffer(wants.getTradedItem(0).orElseThrow(), wants.getTradedItem(1), gives.itemStack(), this.maxUses, this.tradeExperience, this.priceMultiplier);
    }

    private boolean test(LootContext context) {
        return this.merchantPredicate.map(merchantPredicate -> merchantPredicate.test(context))
            .orElse(true);
    }

    private Input createWantedStacks(LootContext context) {
        List<ItemStack> stacks = this.wants.stream().map(entry -> entry.createStack(context)).toList();
        return new Input(stacks);
    }

    private ItemCost createGivenStack(Input wants, LootContext context) {
        ItemStack gives = this.gives.createStack(context);
        return this.tradeModifier.flatMap(tradeModifier -> tradeModifier.apply(wants, gives, context))
            .orElseGet(() -> new ItemCost(gives.getItemHolder(), gives.getCount(), DataComponentExactPredicate.allOf(gives.getComponents())));
    }

    public static Builder builder(Entry gives) {
        return new Builder(gives);
    }

    public static Trade of(Entry firstBuy, Entry sell, int maxUses, int tradeExperience) {
        return of(firstBuy, sell, maxUses, tradeExperience, 0.05f);
    }

    public static Trade of(Entry firstBuy, Entry sell, int maxUses, int tradeExperience, float priceMultiplier) {
        return of(List.of(firstBuy), sell, maxUses, tradeExperience, priceMultiplier, null, null);
    }

    public static Trade of(List<Entry> wants, Entry gives, int maxUses, int tradeExperience, float priceMultiplier, @Nullable TradeModifier<?> tradeModifier, @Nullable LootItemCondition merchantPredicate) {
        if (wants.size() > MAX_WANTED_ENTRIES) {
            throw new IllegalArgumentException("Wanted entries must not be more than " + MAX_WANTED_ENTRIES);
        }

        return new Trade(
            wants,
            gives,
            maxUses,
            tradeExperience,
            priceMultiplier,
            Optional.ofNullable(tradeModifier),
            Optional.ofNullable(merchantPredicate)
        );
    }

    public static class Builder {
        private final List<Entry> wants = new ArrayList<>();
        private final Entry gives;
        private int maxUses = VillagerTradesAccessor.defaultMaxUses();
        private int tradeExperience;
        private float priceMultiplier = VillagerTradesAccessor.lowPriceMultiplier();
        @Nullable
        private TradeModifier<?> tradeModifier;
        @Nullable
        private LootItemCondition merchantPredicate;

        public Builder(Entry gives) {
            this.gives = gives;
        }

        public Trade build() {
            return Trade.of(
                this.wants,
                this.gives,
                this.maxUses,
                this.tradeExperience,
                this.priceMultiplier,
                this.tradeModifier,
                this.merchantPredicate
            );
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

        public Builder merchantPredicate(LootItemCondition.Builder merchantPredicate) {
            this.merchantPredicate = merchantPredicate.build();
            return this;
        }
    }

    public static class Input {
        private final List<ItemStack> stacks;

        private Input(List<ItemStack> stacks) {
            this.stacks = stacks;
        }

        public Optional<ItemCost> getTradedItem(int index) {
            if (index < 0 || index >= this.stacks.size()) {
                return Optional.empty();
            }

            ItemStack stack = this.stacks.get(index);
            return Optional.of(new ItemCost(stack.getItemHolder(), stack.getCount(), DataComponentExactPredicate.allOf(stack.getComponents())));
        }

        public ItemStack getStack(int index) {
            if (index < 0 || index >= this.stacks.size()) {
                return ItemStack.EMPTY;
            }
            return this.stacks.get(index);
        }
    }

    public record Entry(Holder<Item> item, RandomRange.Integers count, Optional<LootItemFunction> itemModifier) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryFixedCodec.create(Registries.ITEM).fieldOf("item").forGetter(Entry::item),
            RandomRange.Integers.CODEC.optionalFieldOf("count", RandomRange.Integers.exactly(1)).forGetter(Entry::count),
            LootItemFunctions.ROOT_CODEC.optionalFieldOf("item_modifier").forGetter(Entry::itemModifier)
        ).apply(instance, Entry::new));

        public ItemStack createStack(LootContext context) {
            int count = Math.clamp(this.count.get(context.getRandom()), 1, this.item.value().getDefaultMaxStackSize());
            ItemStack stack = new ItemStack(this.item, count);
            return this.itemModifier.map(itemModifier -> {
                context.pushVisitedElement(LootContext.createVisitedEntry(itemModifier));
                return itemModifier.apply(stack, context);
            }).orElse(stack);
        }

        public static Entry of(Holder<Item> item) {
            return of(item, 1, null);
        }

        public static Entry of(Holder<Item> item, int count) {
            return of(item, count, null);
        }

        public static Entry of(Holder<Item> item, int count, @Nullable LootItemFunction itemModifier) {
            return new Entry(item, RandomRange.Integers.exactly(count), Optional.ofNullable(itemModifier));
        }

        public static Entry ofEmerald(HolderGetter<Item> items) {
            return ofEmerald(items, 1);
        }

        public static Entry ofEmerald(HolderGetter<Item> items, int count) {
            return of(items.getOrThrow(ItemIds.EMERALD), count);
        }
    }
}
