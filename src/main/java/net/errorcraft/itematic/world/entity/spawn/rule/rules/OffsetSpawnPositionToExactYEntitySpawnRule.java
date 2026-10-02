package net.errorcraft.itematic.world.entity.spawn.rule.rules;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;

public class OffsetSpawnPositionToExactYEntitySpawnRule implements EntitySpawnRule<OffsetSpawnPositionToExactYEntitySpawnRule> {
    public static final OffsetSpawnPositionToExactYEntitySpawnRule INSTANCE = new OffsetSpawnPositionToExactYEntitySpawnRule();
    public static final MapCodec<OffsetSpawnPositionToExactYEntitySpawnRule> CODEC = MapCodec.unit(INSTANCE);

    private OffsetSpawnPositionToExactYEntitySpawnRule() {}

    @Override
    public MapCodec<OffsetSpawnPositionToExactYEntitySpawnRule> codec() {
        return CODEC;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        context.spawnAtExactY();
        return true;
    }
}
