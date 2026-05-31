package net.errorcraft.itematic.entity.spawn.rule.type;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.entity.spawn.rule.EntitySpawnRule;
import net.errorcraft.itematic.entity.spawn.rule.EntitySpawnRuleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

public class FitsInVolumeEntitySpawnRule implements EntitySpawnRule<FitsInVolumeEntitySpawnRule> {
    public static final FitsInVolumeEntitySpawnRule INSTANCE = new FitsInVolumeEntitySpawnRule();
    public static final MapCodec<FitsInVolumeEntitySpawnRule> CODEC = MapCodec.unit(INSTANCE);

    private FitsInVolumeEntitySpawnRule() {}

    @Override
    public EntitySpawnRuleType<FitsInVolumeEntitySpawnRule> type() {
        return EntitySpawnRuleType.FITS_IN_VOLUME;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        ServerWorld world = context.world();
        Box box = context.entityType()
            .getDimensions()
            .getBoxAt(context.spawnPosition());
        return world.isSpaceEmpty(null, box) && world.getOtherEntities(null, box).isEmpty();
    }
}
