package net.errorcraft.itematic.entity.spawn.rule.type;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.entity.spawn.rule.EntitySpawnRule;
import net.errorcraft.itematic.entity.spawn.rule.EntitySpawnRuleType;

public class DiscardEntitySpawnRule implements EntitySpawnRule<DiscardEntitySpawnRule> {
    public static final DiscardEntitySpawnRule INSTANCE = new DiscardEntitySpawnRule();
    public static final MapCodec<DiscardEntitySpawnRule> CODEC = MapCodec.unit(INSTANCE);

    private DiscardEntitySpawnRule() {}

    @Override
    public EntitySpawnRuleType<DiscardEntitySpawnRule> type() {
        return EntitySpawnRuleType.DISCARD;
    }

    @Override
    public boolean apply(EntitySpawnContext context) {
        return false;
    }
}
