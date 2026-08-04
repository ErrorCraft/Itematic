package net.errorcraft.itematic.predicate.item;

import net.errorcraft.itematic.access.predicate.item.ItemPredicateAccess;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.network.codec.PacketCodecUtil;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import java.util.Optional;
import java.util.Set;

public class ItemPredicates {
    private static final StreamCodec<RegistryFriendlyByteBuf, Set<ItemComponentType<?>>> ITEM_BEHAVIOR_PACKET_CODEC = ByteBufCodecs.registry(ItematicRegistryKeys.ITEM_COMPONENT_TYPE)
        .apply(PacketCodecUtil::set);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemPredicate> PACKET_CODEC = StreamCodec.composite(
        ByteBufCodecs.holderSet(Registries.ITEM).apply(ByteBufCodecs::optional), ItemPredicate::items,
        MinMaxBounds.Ints.STREAM_CODEC, ItemPredicate::count,
        DataComponentMatchers.STREAM_CODEC, ItemPredicate::components,
        ITEM_BEHAVIOR_PACKET_CODEC.apply(ByteBufCodecs::optional), ItemPredicateAccess::itematic$behavior,
        ItemPredicates::create
    );

    private ItemPredicates() {}

    public static ItemPredicate setBehavior(ItemPredicate predicate, Optional<Set<ItemComponentType<?>>> behavior) {
        predicate.itematic$setBehavior(behavior);
        return predicate;
    }

    private static ItemPredicate create(Optional<HolderSet<Item>> items, MinMaxBounds.Ints count, DataComponentMatchers components, Optional<Set<ItemComponentType<?>>> behavior) {
        ItemPredicate predicate = new ItemPredicate(items, count, components);
        predicate.itematic$setBehavior(behavior);
        return predicate;
    }
}
