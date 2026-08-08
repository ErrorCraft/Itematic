package net.errorcraft.itematic.world.item.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.predicate.item.ItemPredicates;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;

public record Glider(Optional<ItemPredicate> useableIf) {
    public static final Codec<Glider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemPredicate.CODEC.optionalFieldOf("useable_if").forGetter(Glider::useableIf)
    ).apply(instance, Glider::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Glider> STREAM_CODEC = ItemPredicates.PACKET_CODEC.apply(ByteBufCodecs::optional)
        .map(Glider::new, Glider::useableIf);

    public boolean canUse(ItemStack stack) {
        return this.useableIf.map(useableIf -> useableIf.test(stack))
            .orElse(true);
    }
}
