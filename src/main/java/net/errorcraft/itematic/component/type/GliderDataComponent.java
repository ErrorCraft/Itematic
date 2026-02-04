package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.predicate.item.ItemPredicateUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.item.ItemPredicate;

import java.util.Optional;

public record GliderDataComponent(Optional<ItemPredicate> useableIf) {
    public static final Codec<GliderDataComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemPredicate.CODEC.optionalFieldOf("useable_if").forGetter(GliderDataComponent::useableIf)
    ).apply(instance, GliderDataComponent::new));
    public static final PacketCodec<RegistryByteBuf, GliderDataComponent> PACKET_CODEC = ItemPredicateUtil.PACKET_CODEC.collect(PacketCodecs::optional)
        .xmap(GliderDataComponent::new, GliderDataComponent::useableIf);

    public boolean canUse(ItemStack stack) {
        return this.useableIf.map(useableIf -> useableIf.test(stack))
            .orElse(true);
    }
}
