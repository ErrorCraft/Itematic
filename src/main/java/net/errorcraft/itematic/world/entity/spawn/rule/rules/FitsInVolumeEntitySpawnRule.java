package net.errorcraft.itematic.world.entity.spawn.rule.rules;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record FitsInVolumeEntitySpawnRule(boolean blocks, EntityVolumeCheck entities, Optional<Vec3> volume) implements EntitySpawnRule<FitsInVolumeEntitySpawnRule> {
    public static final MapCodec<FitsInVolumeEntitySpawnRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("blocks", false).forGetter(FitsInVolumeEntitySpawnRule::blocks),
        EntityVolumeCheck.CODEC.optionalFieldOf("entities", new EntityVolumeCheck.Any(false)).forGetter(FitsInVolumeEntitySpawnRule::entities),
        Vec3.CODEC.optionalFieldOf("volume").forGetter(FitsInVolumeEntitySpawnRule::volume)
    ).apply(instance, FitsInVolumeEntitySpawnRule::new));

    public static FitsInVolumeEntitySpawnRule of(boolean blocks, boolean entities, Vec3 volume) {
        return new FitsInVolumeEntitySpawnRule(
            blocks,
            new EntityVolumeCheck.Any(entities),
            Optional.of(volume)
        );
    }

    public static FitsInVolumeEntitySpawnRule entityDimensions() {
        return new FitsInVolumeEntitySpawnRule(
            true,
            new EntityVolumeCheck.Any(true),
            Optional.empty()
        );
    }

    public static FitsInVolumeEntitySpawnRule entityDimensionsForEntities(HolderSet<EntityType<?>> entityTypes) {
        return new FitsInVolumeEntitySpawnRule(
            false,
            new EntityVolumeCheck.OfType(entityTypes),
            Optional.empty()
        );
    }

    @Override
    public MapCodec<FitsInVolumeEntitySpawnRule> codec() {
        return CODEC;
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

        return this.entities.fits(level, box);
    }

    public sealed interface EntityVolumeCheck permits EntityVolumeCheck.Any, EntityVolumeCheck.OfType {
        Codec<EntityVolumeCheck> CODEC = Codec.either(Any.CODEC, OfType.CODEC).xmap(
            Either::unwrap,
            check -> switch (check) {
                case Any any -> Either.left(any);
                case OfType ofType -> Either.right(ofType);
            }
        );

        boolean fits(ServerLevel level, AABB box);

        record Any(boolean check) implements EntityVolumeCheck {
            public static final Codec<Any> CODEC = Codec.BOOL.xmap(Any::new, Any::check);

            @Override
            public boolean fits(ServerLevel level, AABB box) {
                return !this.check || level.getEntities(null, box).isEmpty();
            }
        }

        record OfType(HolderSet<EntityType<?>> entityTypes) implements EntityVolumeCheck {
            public static final Codec<OfType> CODEC = RegistryCodecs.holderSet(Registries.ENTITY_TYPE)
                .xmap(
                    OfType::new,
                    OfType::entityTypes
                );

            @Override
            public boolean fits(ServerLevel level, AABB box) {
                return level.getEntities(
                    (Entity) null,
                    box,
                    entity -> entity.is(this.entityTypes)
                ).isEmpty();
            }
        }
    }
}
