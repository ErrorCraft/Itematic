package net.errorcraft.itematic.loot.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.PositionTarget;

public record LocationCheckLootConditionExtraFields(PositionTarget position) {
    public static final MapCodec<LocationCheckLootConditionExtraFields> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.optionalFieldOf("position", PositionTarget.ORIGIN).forGetter(LocationCheckLootConditionExtraFields::position)
    ).apply(instance, LocationCheckLootConditionExtraFields::new));
    public static final LocationCheckLootConditionExtraFields DEFAULT = new LocationCheckLootConditionExtraFields(PositionTarget.ORIGIN);
}
