package net.errorcraft.itematic.village.trade.modifier.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.village.trade.modifier.TradeModifier;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierType;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.npc.villager.VillagerDataHolder;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import java.util.Map;
import java.util.Optional;

public record ItemFromTypeTradeModifier(Map<Holder<VillagerType>, Holder<Item>> types) implements TradeModifier<ItemFromTypeTradeModifier> {
    public static final MapCodec<ItemFromTypeTradeModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.simpleMap(BuiltInRegistries.VILLAGER_TYPE.holderByNameCodec(), RegistryFixedCodec.create(Registries.ITEM), BuiltInRegistries.VILLAGER_TYPE).fieldOf("types").forGetter(ItemFromTypeTradeModifier::types)
    ).apply(instance, ItemFromTypeTradeModifier::new));

    public static ItemFromTypeTradeModifier of(Map<Holder<VillagerType>, Holder<Item>> types) {
        return new ItemFromTypeTradeModifier(types);
    }

    @Override
    public TradeModifierType<ItemFromTypeTradeModifier> type() {
        return TradeModifierTypes.ITEM_FROM_TYPE;
    }

    @Override
    public Optional<ItemCost> apply(Trade.Input wants, ItemStack gives, LootContext context) {
        if (!(context.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof VillagerDataHolder container)) {
            return Optional.empty();
        }

        Holder<VillagerType> type = container.getVillagerData().type();
        if (!this.types.containsKey(type)) {
            return Optional.empty();
        }

        ItemStack givesActual = gives.itematic$copyWithItem(this.types.get(type));
        return Optional.of(new ItemCost(givesActual.getItemHolder(), givesActual.getCount(), DataComponentExactPredicate.allOf(givesActual.getComponents())));
    }
}
