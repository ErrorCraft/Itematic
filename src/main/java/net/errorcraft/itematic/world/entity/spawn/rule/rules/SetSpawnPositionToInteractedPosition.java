package net.errorcraft.itematic.world.entity.spawn.rule.rules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;
import net.minecraft.world.phys.Vec3;

public record SetSpawnPositionToInteractedPosition(boolean x, boolean y, boolean z) implements EntitySpawnRule<SetSpawnPositionToInteractedPosition> {
    public static final MapCodec<SetSpawnPositionToInteractedPosition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("x", false).forGetter(SetSpawnPositionToInteractedPosition::x),
        Codec.BOOL.optionalFieldOf("y", false).forGetter(SetSpawnPositionToInteractedPosition::y),
        Codec.BOOL.optionalFieldOf("z", false).forGetter(SetSpawnPositionToInteractedPosition::z)
    ).apply(instance, SetSpawnPositionToInteractedPosition::new));

    public static SetSpawnPositionToInteractedPosition of(boolean x, boolean y, boolean z) {
        return new SetSpawnPositionToInteractedPosition(x, y, z);
    }

    @Override
    public MapCodec<SetSpawnPositionToInteractedPosition> codec() {
        return CODEC;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        Vec3 exactPosition = context.interactedPosition();
        context.spawnPosition(spawnPosition -> new Vec3(
            this.x ? exactPosition.x() : spawnPosition.x(),
            this.y ? exactPosition.y() : spawnPosition.y(),
            this.z ? exactPosition.z() : spawnPosition.z()
        ));
        return true;
    }
}
