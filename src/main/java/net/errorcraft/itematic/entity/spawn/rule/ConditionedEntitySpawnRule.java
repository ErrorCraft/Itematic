package net.errorcraft.itematic.entity.spawn.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.spawn.EntitySpawnContext;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;

import java.util.Optional;

public record ConditionedEntitySpawnRule(EntitySpawnRule<?> rule, Optional<LootCondition> condition) {
    public static final Codec<ConditionedEntitySpawnRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntitySpawnRule.CODEC.forGetter(ConditionedEntitySpawnRule::rule),
        LootCondition.CODEC.optionalFieldOf("condition").forGetter(ConditionedEntitySpawnRule::condition)
    ).apply(instance, ConditionedEntitySpawnRule::new));

    public static ConditionedEntitySpawnRule of(EntitySpawnRule<?> rule) {
        return new ConditionedEntitySpawnRule(rule, Optional.empty());
    }

    public static ConditionedEntitySpawnRule of(EntitySpawnRule<?> rule, LootCondition condition) {
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
