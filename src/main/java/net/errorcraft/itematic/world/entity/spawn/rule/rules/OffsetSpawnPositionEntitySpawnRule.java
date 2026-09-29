package net.errorcraft.itematic.world.entity.spawn.rule.rules;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;
import net.minecraft.world.phys.Vec3;

public record OffsetSpawnPositionEntitySpawnRule(Vec3 offset) implements EntitySpawnRule<OffsetSpawnPositionEntitySpawnRule> {
    public static final MapCodec<OffsetSpawnPositionEntitySpawnRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Vec3.CODEC.fieldOf("offset").forGetter(OffsetSpawnPositionEntitySpawnRule::offset)
    ).apply(instance, OffsetSpawnPositionEntitySpawnRule::new));

    public static OffsetSpawnPositionEntitySpawnRule of(Vec3 offset) {
        return new OffsetSpawnPositionEntitySpawnRule(offset);
    }

    @Override
    public MapCodec<OffsetSpawnPositionEntitySpawnRule> codec() {
        return CODEC;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        context.spawnPosition(context.spawnPosition().add(this.offset));
        return true;
    }
}
