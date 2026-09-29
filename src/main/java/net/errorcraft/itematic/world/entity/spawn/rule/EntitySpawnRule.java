package net.errorcraft.itematic.world.entity.spawn.rule;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;

import java.util.function.Function;

public interface EntitySpawnRule<T extends EntitySpawnRule<T>> {
    MapCodec<EntitySpawnRule<?>> CODEC = ItematicBuiltInRegistries.ENTITY_SPAWN_RULE_TYPE.byNameCodec()
        .dispatchMap(EntitySpawnRule::codec, Function.identity());

    MapCodec<T> codec();
    boolean apply(EntitySpawnContext context);
}
