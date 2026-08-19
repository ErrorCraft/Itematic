package net.errorcraft.itematic.world.item.trading.modifier.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.trading.Trade;
import net.errorcraft.itematic.world.item.trading.modifier.TradeModifier;
import net.errorcraft.itematic.world.item.trading.modifier.TradeModifierType;
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
        return TradeModifierType.ITEM_FROM_TYPE;
    }

    @Override
    public Optional<ItemCost> apply(Trade.Input wants, ItemStack gives, LootContext context) {
        if (!(context.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof VillagerDataHolder villagerDataHolder)) {
            return Optional.empty();
        }

        Holder<VillagerType> type = villagerDataHolder.getVillagerData().type();
        if (!this.types.containsKey(type)) {
            return Optional.empty();
        }

        ItemStack givesActual = gives.itematic$transmuteCopy(this.types.get(type));
        return Optional.of(
            new ItemCost(
                givesActual.getItemHolder(),
                givesActual.getCount(),
                DataComponentExactPredicate.allOf(givesActual.getComponents())
            )
        );
    }
}
