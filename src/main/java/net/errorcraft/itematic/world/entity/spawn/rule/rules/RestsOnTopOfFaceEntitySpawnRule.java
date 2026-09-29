package net.errorcraft.itematic.world.entity.spawn.rule.rules;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Cushion;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record RestsOnTopOfFaceEntitySpawnRule(Optional<Vec3> volume) implements EntitySpawnRule<RestsOnTopOfFaceEntitySpawnRule> {
    public static final MapCodec<RestsOnTopOfFaceEntitySpawnRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Vec3.CODEC.optionalFieldOf("volume").forGetter(RestsOnTopOfFaceEntitySpawnRule::volume)
    ).apply(instance, RestsOnTopOfFaceEntitySpawnRule::new));

    public static RestsOnTopOfFaceEntitySpawnRule entityDimensions() {
        return new RestsOnTopOfFaceEntitySpawnRule(Optional.empty());
    }

    @Override
    public MapCodec<RestsOnTopOfFaceEntitySpawnRule> codec() {
        return CODEC;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        return Cushion.wouldSuriveAt(
            context.level(),
            this.box(context.spawnPosition(), context.entityType())
        );
    }

    private AABB box(Vec3 spawnPosition, EntityType<?> type) {
        if (this.volume.isPresent()) {
            Vec3 volume = this.volume.get();
            return AABB.ofSize(spawnPosition, volume.x(), volume.y(), volume.z());
        }

        return type.getDimensions().makeBoundingBox(spawnPosition);
    }
}
