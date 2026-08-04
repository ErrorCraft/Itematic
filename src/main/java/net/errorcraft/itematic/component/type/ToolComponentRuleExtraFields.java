package net.errorcraft.itematic.component.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.predicate.item.ItemPredicates;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import java.util.Optional;

public record ToolComponentRuleExtraFields(Optional<ItemPredicate> item) {
    public static final MapCodec<ToolComponentRuleExtraFields> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemPredicate.CODEC.optionalFieldOf("item").forGetter(ToolComponentRuleExtraFields::item)
    ).apply(instance, ToolComponentRuleExtraFields::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToolComponentRuleExtraFields> PACKET_CODEC = StreamCodec.composite(
        ItemPredicates.PACKET_CODEC.apply(ByteBufCodecs::optional), ToolComponentRuleExtraFields::item,
        ToolComponentRuleExtraFields::new
    );
}
