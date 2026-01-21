package net.errorcraft.itematic.predicate.item;

import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.access.predicate.item.ItemPredicateAccess;
import net.errorcraft.itematic.mixin.predicate.NumberRangeAccessor;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.ComponentPredicate;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.predicate.item.ItemSubPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.Map;
import java.util.Optional;

public class ItemPredicateUtil {
    private static final PacketCodec<ByteBuf, NumberRange.IntRange> INTEGER_RANGE_PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.optional(PacketCodecs.VAR_INT), NumberRange.IntRange::min,
        PacketCodecs.optional(PacketCodecs.VAR_INT), NumberRange.IntRange::max,
        NumberRangeAccessor.IntRangeAccessor::create
    );
    public static final PacketCodec<RegistryByteBuf, ItemPredicate> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.optional(PacketCodecs.registryEntryList(RegistryKeys.ITEM)), ItemPredicate::items,
        INTEGER_RANGE_PACKET_CODEC, ItemPredicate::count,
        ComponentPredicate.PACKET_CODEC, ItemPredicate::components,
        PacketCodecs.registryCodec(ItemSubPredicate.PREDICATES_MAP_CODEC), ItemPredicate::subPredicates,
        ItemPredicateExtraFields.PACKET_CODEC, itemPredicate -> ((ItemPredicateAccess)(Object) itemPredicate).itematic$extraFields(),
        ItemPredicateUtil::create
    );

    private static ItemPredicate create(Optional<RegistryEntryList<Item>> items, NumberRange.IntRange count, ComponentPredicate components, Map<ItemSubPredicate.Type<?>, ItemSubPredicate> subPredicates, ItemPredicateExtraFields extraFields) {
        ItemPredicate predicate = new ItemPredicate(items, count, components, subPredicates);
        ((ItemPredicateAccess)(Object) predicate).itematic$setExtraFields(extraFields);
        return predicate;
    }

    private ItemPredicateUtil() {}
}
