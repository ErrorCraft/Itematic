package net.errorcraft.itematic.predicate.item;

import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.mixin.predicate.NumberRangeAccessor;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.ComponentPredicate;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.predicate.item.ItemSubPredicate;
import net.minecraft.registry.RegistryKeys;

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
        ItemPredicate::new
    );

    private ItemPredicateUtil() {}
}
