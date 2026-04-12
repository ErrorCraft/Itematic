package net.errorcraft.itematic.predicate.item;

import net.errorcraft.itematic.access.predicate.item.ItemPredicateAccess;
import net.errorcraft.itematic.predicate.NumberRangeUtil;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.component.ComponentsPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.Optional;

public class ItemPredicateUtil {
    public static final PacketCodec<RegistryByteBuf, ItemPredicate> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.optional(PacketCodecs.registryEntryList(RegistryKeys.ITEM)), ItemPredicate::items,
        NumberRangeUtil.INTEGER_RANGE_PACKET_CODEC, ItemPredicate::count,
        ComponentsPredicate.PACKET_CODEC, ItemPredicate::components,
        ItemPredicateExtraFields.PACKET_CODEC, itemPredicate -> ((ItemPredicateAccess)(Object) itemPredicate).itematic$extraFields(),
        ItemPredicateUtil::create
    );

    private ItemPredicateUtil() {}

    private static ItemPredicate create(Optional<RegistryEntryList<Item>> items, NumberRange.IntRange count, ComponentsPredicate components, ItemPredicateExtraFields extraFields) {
        ItemPredicate predicate = new ItemPredicate(items, count, components);
        ((ItemPredicateAccess)(Object) predicate).itematic$setExtraFields(extraFields);
        return predicate;
    }
}
