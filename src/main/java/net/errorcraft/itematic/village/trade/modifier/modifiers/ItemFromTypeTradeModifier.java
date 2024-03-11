package net.errorcraft.itematic.village.trade.modifier.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.village.trade.modifier.TradeModifier;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierType;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.ComponentPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerType;

import java.util.Map;
import java.util.Optional;

public record ItemFromTypeTradeModifier(Map<VillagerType, RegistryEntry<Item>> types) implements TradeModifier<ItemFromTypeTradeModifier> {
    public static final Codec<ItemFromTypeTradeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.simpleMap(Registries.VILLAGER_TYPE.getCodec(), RegistryFixedCodec.of(RegistryKeys.ITEM), Registries.VILLAGER_TYPE).fieldOf("types").forGetter(ItemFromTypeTradeModifier::types)
    ).apply(instance, ItemFromTypeTradeModifier::new));

    @Override
    public TradeModifierType<ItemFromTypeTradeModifier> type() {
        return TradeModifierTypes.ITEM_FROM_TYPE;
    }

    @Override
    public Optional<TradedItem> apply(Trade.Input wants, ItemStack gives, LootContext context) {
        if (!(context.get(LootContextParameters.THIS_ENTITY) instanceof VillagerDataContainer container)) {
            return Optional.empty();
        }
        VillagerType type = container.getVillagerData().getType();
        if (!this.types.containsKey(type)) {
            return Optional.empty();
        }
        ItemStack givesActual = gives.itematic$copyWithItem(this.types.get(type));
        return Optional.of(new TradedItem(givesActual.getRegistryEntry(), givesActual.getCount(), ComponentPredicate.of(givesActual.getComponents())));
    }

    public static ItemFromTypeTradeModifier of(Map<VillagerType, RegistryEntry<Item>> types) {
        return new ItemFromTypeTradeModifier(types);
    }
}
