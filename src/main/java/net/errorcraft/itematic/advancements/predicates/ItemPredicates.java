package net.errorcraft.itematic.advancements.predicates;

import net.errorcraft.itematic.network.codec.ItematicStreamCodecs;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;

import java.util.Optional;
import java.util.Set;

public class ItemPredicates {
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemPredicate> STREAM_CODEC = StreamCodec.recursive(c -> StreamCodec.composite(
        ByteBufCodecs.holderSet(Registries.ITEM).apply(ByteBufCodecs::optional), ItemPredicate::items,
        MinMaxBounds.Ints.STREAM_CODEC, ItemPredicate::count,
        DataComponentMatchers.STREAM_CODEC, ItemPredicate::components,
        ItemBehaviorType.STREAM_CODEC.apply(ItematicStreamCodecs::set).apply(ByteBufCodecs::optional), ItemPredicate::itematic$behavior,
        ItemPredicates::create
    ));

    private ItemPredicates() {}

    private static ItemPredicate create(Optional<HolderSet<Item>> items, MinMaxBounds.Ints count, DataComponentMatchers components, Optional<Set<ItemBehaviorType<?>>> behavior) {
        ItemPredicate predicate = new ItemPredicate(items, count, components);
        predicate.itematic$setBehavior(behavior);
        return predicate;
    }
}
