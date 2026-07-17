package net.errorcraft.itematic.predicate.item;

import net.errorcraft.itematic.access.predicate.item.ItemPredicateAccess;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.network.codec.PacketCodecUtil;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
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
import java.util.Set;

public class ItemPredicates {
    private static final PacketCodec<RegistryByteBuf, Set<ItemComponentType<?>>> ITEM_BEHAVIOR_PACKET_CODEC = PacketCodecs.registryValue(ItematicRegistryKeys.ITEM_COMPONENT_TYPE)
        .collect(PacketCodecUtil::set);
    public static final PacketCodec<RegistryByteBuf, ItemPredicate> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.registryEntryList(RegistryKeys.ITEM).collect(PacketCodecs::optional), ItemPredicate::items,
        NumberRange.IntRange.PACKET_CODEC, ItemPredicate::count,
        ComponentsPredicate.PACKET_CODEC, ItemPredicate::components,
        ITEM_BEHAVIOR_PACKET_CODEC.collect(PacketCodecs::optional), ItemPredicateAccess::itematic$behavior,
        ItemPredicates::create
    );

    private ItemPredicates() {}

    public static ItemPredicate setBehavior(ItemPredicate predicate, Optional<Set<ItemComponentType<?>>> behavior) {
        predicate.itematic$setBehavior(behavior);
        return predicate;
    }

    private static ItemPredicate create(Optional<RegistryEntryList<Item>> items, NumberRange.IntRange count, ComponentsPredicate components, Optional<Set<ItemComponentType<?>>> behavior) {
        ItemPredicate predicate = new ItemPredicate(items, count, components);
        predicate.itematic$setBehavior(behavior);
        return predicate;
    }
}
