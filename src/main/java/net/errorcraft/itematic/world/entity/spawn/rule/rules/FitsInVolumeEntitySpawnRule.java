package net.errorcraft.itematic.world.entity.spawn.rule.rules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRuleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;

public record FitsInVolumeEntitySpawnRule(boolean blocks, boolean entities, Optional<Vec3> volume) implements EntitySpawnRule<FitsInVolumeEntitySpawnRule> {
    public static final MapCodec<FitsInVolumeEntitySpawnRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("blocks", true).forGetter(FitsInVolumeEntitySpawnRule::blocks),
        Codec.BOOL.optionalFieldOf("entities", true).forGetter(FitsInVolumeEntitySpawnRule::entities),
        Vec3.CODEC.optionalFieldOf("volume").forGetter(FitsInVolumeEntitySpawnRule::volume)
    ).apply(instance, FitsInVolumeEntitySpawnRule::new));

    public static FitsInVolumeEntitySpawnRule of(boolean blocks, boolean entities, Vec3 volume) {
        return new FitsInVolumeEntitySpawnRule(blocks, entities, Optional.of(volume));
    }

    public static FitsInVolumeEntitySpawnRule entityDimensions() {
        return new FitsInVolumeEntitySpawnRule(true, true, Optional.empty());
    }

    @Override
    public EntitySpawnRuleType<FitsInVolumeEntitySpawnRule> type() {
        return EntitySpawnRuleType.FITS_IN_VOLUME;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        AABB box = this.box(context.spawnPosition(), context.entityType());
        return this.fits(context.level(), box);
    }

    private AABB box(Vec3 spawnPosition, EntityType<?> type) {
        if (this.volume.isPresent()) {
            Vec3 volume = this.volume.get();
            return AABB.ofSize(spawnPosition, volume.x(), volume.y(), volume.z());
        }

        return type.getDimensions().makeBoundingBox(spawnPosition);
    }

    private boolean fits(ServerLevel level, AABB box) {
        if (this.blocks && !level.noCollision(null, box)) {
            return false;
        }

        return !this.entities || level.getEntities(null, box).isEmpty();
    }
}
