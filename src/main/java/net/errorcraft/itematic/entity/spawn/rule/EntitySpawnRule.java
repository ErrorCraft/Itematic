package net.errorcraft.itematic.entity.spawn.rule;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.registry.ItematicRegistries;

public interface EntitySpawnRule<T extends EntitySpawnRule<T>> {
    MapCodec<EntitySpawnRule<?>> CODEC = ItematicRegistries.ENTITY_SPAWN_RULE_TYPE.getCodec().dispatchMap(EntitySpawnRule::type, EntitySpawnRuleType::codec);

    EntitySpawnRuleType<T> type();
    boolean apply(EntitySpawnContext context);
}
