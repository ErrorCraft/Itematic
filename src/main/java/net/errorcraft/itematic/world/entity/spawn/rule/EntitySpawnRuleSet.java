package net.errorcraft.itematic.world.entity.spawn.rule;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawnContext;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.ArrayList;
import java.util.List;

public class EntitySpawnRuleSet {
    public static final Codec<EntitySpawnRuleSet> DIRECT_CODEC = ConditionedEntitySpawnRule.CODEC.listOf()
        .xmap(
            EntitySpawnRuleSet::new,
            set -> set.spawnRules
        );
    public static final Codec<Holder<EntitySpawnRuleSet>> CODEC = RegistryCodecs.holder(ItematicRegistries.ENTITY_SPAWN_RULE_SET);

    private final List<ConditionedEntitySpawnRule> spawnRules;

    private EntitySpawnRuleSet(List<ConditionedEntitySpawnRule> spawnRules) {
        this.spawnRules = spawnRules;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean applyRules(LootContext predicateContext, EntitySpawnContext spawnContext) {
        for (ConditionedEntitySpawnRule spawnRule : this.spawnRules) {
            if (!spawnRule.apply(predicateContext, spawnContext)) {
                return false;
            }
        }

        return true;
    }

    public static class Builder {
        private final List<ConditionedEntitySpawnRule> spawnRules = new ArrayList<>();

        private Builder() {}

        public EntitySpawnRuleSet build() {
            return new EntitySpawnRuleSet(this.spawnRules);
        }

        public Builder add(EntitySpawnRule<?> rule) {
            this.spawnRules.add(ConditionedEntitySpawnRule.of(rule));
            return this;
        }

        public Builder add(EntitySpawnRule<?> rule, LootItemCondition.Builder condition) {
            this.spawnRules.add(ConditionedEntitySpawnRule.of(rule, condition.build()));
            return this;
        }
    }
}
