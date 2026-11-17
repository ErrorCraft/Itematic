package net.errorcraft.itematic.advancements.predicates;

import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class ItemPredicates {
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemPredicate> STREAM_CODEC = StreamCodec.recursive(c -> StreamCodec.composite(
        ByteBufCodecs.holderSet(Registries.ITEM).apply(ByteBufCodecs::optional), ItemPredicate::items,
        MinMaxBounds.Ints.STREAM_CODEC, ItemPredicate::count,
        DataComponentMatchers.STREAM_CODEC, ItemPredicate::components,
        ItemPredicate::new
    ));

    private ItemPredicates() {}
}
