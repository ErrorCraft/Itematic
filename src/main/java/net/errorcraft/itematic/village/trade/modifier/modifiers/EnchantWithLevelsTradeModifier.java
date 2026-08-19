package net.errorcraft.itematic.village.trade.modifier.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.RandomRange;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.village.trade.modifier.TradeModifier;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierType;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierTypes;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.Optional;

public record EnchantWithLevelsTradeModifier(int index, RandomRange.Integers level, boolean treasure) implements TradeModifier<EnchantWithLevelsTradeModifier> {
    public static final MapCodec<EnchantWithLevelsTradeModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Trade.WANTED_INDEX_CODEC.fieldOf("index").forGetter(EnchantWithLevelsTradeModifier::index),
        RandomRange.Integers.CODEC.fieldOf("level").forGetter(EnchantWithLevelsTradeModifier::level),
        Codec.BOOL.optionalFieldOf("treasure", false).forGetter(EnchantWithLevelsTradeModifier::treasure)
    ).apply(instance, EnchantWithLevelsTradeModifier::new));

    public static EnchantWithLevelsTradeModifier of(int index, int minLevel, int maxLevel) {
        return new EnchantWithLevelsTradeModifier(index, RandomRange.Integers.of(minLevel, maxLevel), false);
    }

    @Override
    public TradeModifierType<EnchantWithLevelsTradeModifier> type() {
        return TradeModifierTypes.ENCHANT_WITH_LEVELS;
    }

    @Override
    public Optional<ItemCost> apply(Trade.Input wants, ItemStack gives, LootContext context) {
        RandomSource random = context.getRandom();
        int level = Math.max(1, this.level.get(random));
        RegistryAccess registryManager = context.getLevel().registryAccess();
        Optional<HolderSet.Named<Enchantment>> enchantments = registryManager.lookupOrThrow(Registries.ENCHANTMENT)
            .get(EnchantmentTags.ON_TRADED_EQUIPMENT);
        ItemStack givesActual = EnchantmentHelper.enchantItem(random, gives, level, registryManager, enchantments);
        wants.getStack(this.index).itematic$tryIncrement(level);
        return Optional.of(new ItemCost(givesActual.getItemHolder(), givesActual.getCount(), DataComponentExactPredicate.allOf(givesActual.getComponents())));
    }
}
