package net.errorcraft.itematic.world.entity.spawn.rule.rules;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;

public class DiscardEntitySpawnRule implements EntitySpawnRule<DiscardEntitySpawnRule> {
    public static final DiscardEntitySpawnRule INSTANCE = new DiscardEntitySpawnRule();
    public static final MapCodec<DiscardEntitySpawnRule> CODEC = MapCodec.unit(INSTANCE);

    private DiscardEntitySpawnRule() {}

    @Override
    public MapCodec<DiscardEntitySpawnRule> codec() {
        return CODEC;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        return false;
    }
}
