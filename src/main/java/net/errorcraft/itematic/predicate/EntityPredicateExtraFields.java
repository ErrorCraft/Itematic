package net.errorcraft.itematic.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public record EntityPredicateExtraFields(Optional<NumberRange.IntRange> usedItemTicks, Optional<Boolean> inWaterOrRain) {
    public static final MapCodec<EntityPredicateExtraFields> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codecs.createStrictOptionalFieldCodec(NumberRange.IntRange.CODEC, "used_item_ticks").forGetter(EntityPredicateExtraFields::usedItemTicks),
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "in_water_or_rain").forGetter(EntityPredicateExtraFields::inWaterOrRain)
    ).apply(instance, EntityPredicateExtraFields::new));

    public static EntityPredicateExtraFields of(NumberRange.IntRange itemUsedTicks, Boolean inWaterOrRain) {
        return new EntityPredicateExtraFields(Optional.ofNullable(itemUsedTicks), Optional.ofNullable(inWaterOrRain));
    }
}
