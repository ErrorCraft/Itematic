package net.errorcraft.itematic.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;

import java.util.Optional;

public record EntityPredicateExtraFields(Optional<MinMaxBounds.Ints> usedItemTicks, Optional<Boolean> inWaterOrRain) {
    public static final MapCodec<EntityPredicateExtraFields> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        MinMaxBounds.Ints.CODEC.optionalFieldOf("used_item_ticks").forGetter(EntityPredicateExtraFields::usedItemTicks),
        Codec.BOOL.optionalFieldOf("in_water_or_rain").forGetter(EntityPredicateExtraFields::inWaterOrRain)
    ).apply(instance, EntityPredicateExtraFields::new));

    public static EntityPredicateExtraFields of(MinMaxBounds.Ints itemUsedTicks, Boolean inWaterOrRain) {
        return new EntityPredicateExtraFields(Optional.ofNullable(itemUsedTicks), Optional.ofNullable(inWaterOrRain));
    }
}
