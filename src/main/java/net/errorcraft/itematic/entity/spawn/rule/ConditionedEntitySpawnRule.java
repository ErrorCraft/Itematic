package net.errorcraft.itematic.entity.spawn.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.spawn.EntitySpawnContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.Optional;

public record ConditionedEntitySpawnRule(EntitySpawnRule<?> rule, Optional<LootItemCondition> condition) {
    public static final Codec<ConditionedEntitySpawnRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntitySpawnRule.CODEC.forGetter(ConditionedEntitySpawnRule::rule),
        LootItemCondition.DIRECT_CODEC.optionalFieldOf("condition").forGetter(ConditionedEntitySpawnRule::condition)
    ).apply(instance, ConditionedEntitySpawnRule::new));

    public static ConditionedEntitySpawnRule of(EntitySpawnRule<?> rule) {
        return new ConditionedEntitySpawnRule(rule, Optional.empty());
    }

    public static ConditionedEntitySpawnRule of(EntitySpawnRule<?> rule, LootItemCondition condition) {
        return new ConditionedEntitySpawnRule(rule, Optional.of(condition));
    }

    public boolean apply(LootContext predicateContext, EntitySpawnContext spawnContext) {
        if (this.test(predicateContext)) {
            return this.rule.apply(spawnContext);
        }

        return true;
    }

    private boolean test(LootContext context) {
        return this.condition.map(p -> p.test(context)).orElse(true);
    }
}
