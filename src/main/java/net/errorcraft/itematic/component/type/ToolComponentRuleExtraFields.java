package net.errorcraft.itematic.component.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.predicate.item.ItemPredicateUtil;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.item.ItemPredicate;

import java.util.Optional;

public record ToolComponentRuleExtraFields(Optional<ItemPredicate> item) {
    public static final MapCodec<ToolComponentRuleExtraFields> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemPredicate.CODEC.optionalFieldOf("item").forGetter(ToolComponentRuleExtraFields::item)
    ).apply(instance, ToolComponentRuleExtraFields::new));
    public static final PacketCodec<RegistryByteBuf, ToolComponentRuleExtraFields> PACKET_CODEC = PacketCodec.tuple(
        ItemPredicateUtil.PACKET_CODEC.collect(PacketCodecs::optional), ToolComponentRuleExtraFields::item,
        ToolComponentRuleExtraFields::new
    );
}
