package net.errorcraft.itematic.entity.spawn.rule.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.entity.spawn.rule.EntitySpawnRule;
import net.errorcraft.itematic.entity.spawn.rule.EntitySpawnRuleType;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public record FitsInVolumeEntitySpawnRule(boolean blocks, boolean entities, Optional<Vec3d> volume) implements EntitySpawnRule<FitsInVolumeEntitySpawnRule> {
    public static final MapCodec<FitsInVolumeEntitySpawnRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("blocks", true).forGetter(FitsInVolumeEntitySpawnRule::blocks),
        Codec.BOOL.optionalFieldOf("entities", true).forGetter(FitsInVolumeEntitySpawnRule::entities),
        Vec3d.CODEC.optionalFieldOf("volume").forGetter(FitsInVolumeEntitySpawnRule::volume)
    ).apply(instance, FitsInVolumeEntitySpawnRule::new));

    public static FitsInVolumeEntitySpawnRule of(boolean blocks, boolean entities, Vec3d volume) {
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
        Box box = this.box(context.spawnPosition(), context.entityType());
        return this.fits(context.world(), box);
    }

    private Box box(Vec3d spawnPosition, EntityType<?> type) {
        if (this.volume.isPresent()) {
            Vec3d volume = this.volume.get();
            return Box.of(spawnPosition, volume.getX(), volume.getY(), volume.getZ());
        }

        return type.getDimensions().getBoxAt(spawnPosition);
    }

    private boolean fits(ServerWorld world, Box box) {
        if (this.blocks && !world.isSpaceEmpty(null, box)) {
            return false;
        }

        return !this.entities || world.getOtherEntities(null, box).isEmpty();
    }
}
