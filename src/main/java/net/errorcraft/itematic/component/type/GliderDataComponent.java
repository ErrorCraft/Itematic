package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.predicate.item.ItemPredicates;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;

public record GliderDataComponent(Optional<ItemPredicate> useableIf) {
    public static final Codec<GliderDataComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemPredicate.CODEC.optionalFieldOf("useable_if").forGetter(GliderDataComponent::useableIf)
    ).apply(instance, GliderDataComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GliderDataComponent> PACKET_CODEC = ItemPredicates.PACKET_CODEC.apply(ByteBufCodecs::optional)
        .map(GliderDataComponent::new, GliderDataComponent::useableIf);

    public boolean canUse(ItemStack stack) {
        return this.useableIf.map(useableIf -> useableIf.test(stack))
            .orElse(true);
    }
}
