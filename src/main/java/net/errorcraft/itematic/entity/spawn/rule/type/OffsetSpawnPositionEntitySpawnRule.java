package net.errorcraft.itematic.entity.spawn.rule.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.entity.spawn.rule.EntitySpawnRule;
import net.errorcraft.itematic.entity.spawn.rule.EntitySpawnRuleType;
import net.minecraft.util.math.Vec3d;

public record OffsetSpawnPositionEntitySpawnRule(Vec3d offset) implements EntitySpawnRule<OffsetSpawnPositionEntitySpawnRule> {
    public static final MapCodec<OffsetSpawnPositionEntitySpawnRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Vec3d.CODEC.fieldOf("offset").forGetter(OffsetSpawnPositionEntitySpawnRule::offset)
    ).apply(instance, OffsetSpawnPositionEntitySpawnRule::new));

    public static OffsetSpawnPositionEntitySpawnRule of(Vec3d offset) {
        return new OffsetSpawnPositionEntitySpawnRule(offset);
    }

    @Override
    public EntitySpawnRuleType<OffsetSpawnPositionEntitySpawnRule> type() {
        return EntitySpawnRuleType.OFFSET_SPAWN_POSITION;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        context.spawnPosition(context.spawnPosition().add(this.offset));
        return true;
    }
}
