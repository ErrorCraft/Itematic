package net.errorcraft.itematic.world.entity.spawn.rule.rules;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;
import net.minecraft.world.phys.Vec3;

public record OffsetSpawnPosition(Vec3 offset) implements EntitySpawnRule<OffsetSpawnPosition> {
    public static final MapCodec<OffsetSpawnPosition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Vec3.CODEC.fieldOf("offset").forGetter(OffsetSpawnPosition::offset)
    ).apply(instance, OffsetSpawnPosition::new));

    public static OffsetSpawnPosition of(Vec3 offset) {
        return new OffsetSpawnPosition(offset);
    }

    @Override
    public MapCodec<OffsetSpawnPosition> codec() {
        return CODEC;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        context.spawnPosition(spawnPosition -> spawnPosition.add(this.offset));
        return true;
    }
}
