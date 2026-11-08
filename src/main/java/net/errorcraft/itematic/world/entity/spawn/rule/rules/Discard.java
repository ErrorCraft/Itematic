package net.errorcraft.itematic.world.entity.spawn.rule.rules;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;

public class Discard implements EntitySpawnRule<Discard> {
    public static final Discard INSTANCE = new Discard();
    public static final MapCodec<Discard> CODEC = MapCodec.unit(INSTANCE);

    private Discard() {}

    @Override
    public MapCodec<Discard> codec() {
        return CODEC;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        return false;
    }
}
