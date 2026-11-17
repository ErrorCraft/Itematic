package net.errorcraft.itematic.world.entity.spawn.rule.rules;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;
import net.minecraft.core.Direction;

public record OffsetSpawnPositionByInteractedSide(double distance) implements EntitySpawnRule<OffsetSpawnPositionByInteractedSide> {
    public static final MapCodec<OffsetSpawnPositionByInteractedSide> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItematicCodecs.POSITIVE_DOUBLE.fieldOf("distance").forGetter(OffsetSpawnPositionByInteractedSide::distance)
    ).apply(instance, OffsetSpawnPositionByInteractedSide::new));

    public static OffsetSpawnPositionByInteractedSide of(double distance) {
        return new OffsetSpawnPositionByInteractedSide(distance);
    }

    @Override
    public MapCodec<OffsetSpawnPositionByInteractedSide> codec() {
        return CODEC;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        Direction interactedSide = context.interactedSide();
        if (interactedSide != null) {
            context.spawnPosition(spawnPosition -> spawnPosition
                .add(interactedSide.getUnitVec3().scale(this.distance))
            );
        }

        return true;
    }
}
