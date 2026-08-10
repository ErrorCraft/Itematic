package net.errorcraft.itematic.world.entity.spawn;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.mixin.entity.EntityAccessor;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.entity.EntitySpawnCallback;
import net.errorcraft.itematic.world.entity.spawn.rule.ConditionedEntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record EntitySpawner(Holder<EntityType<?>> entity, List<ConditionedEntitySpawnRule> spawnRules, DataComponentPatch components, Optional<Holder<SoundEvent>> spawnSound, boolean allowItemData) {
    public static final Codec<EntitySpawner> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().fieldOf("entity").forGetter(EntitySpawner::entity),
        ConditionedEntitySpawnRule.CODEC.listOf().optionalFieldOf("spawn_rules", List.of()).forGetter(EntitySpawner::spawnRules),
        DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(EntitySpawner::components),
        SoundEvent.CODEC.optionalFieldOf("spawn_sound").forGetter(EntitySpawner::spawnSound),
        Codec.BOOL.optionalFieldOf("allow_item_data", false).forGetter(EntitySpawner::allowItemData)
    ).apply(instance, EntitySpawner::new));

    public static EntitySpawner of(Holder<EntityType<?>> entity) {
        return EntitySpawner.builder(entity).build();
    }

    public static Builder builder(Holder<EntityType<?>> entity) {
        return new Builder(entity);
    }

    public EntityType<?> entityType(ItemStack stack) {
        if (!this.allowItemData) {
            return this.entity.value();
        }

        TypedEntityData<EntityType<?>> entityData = stack.get(DataComponents.ENTITY_DATA);
        if (entityData != null) {
            return entityData.type();
        }

        return this.entity.value();
    }

    @Nullable
    public Entity spawn(ActionContext context, Vec3 initialPos, EntitySpawnReason spawnReason, EntitySpawnCallback spawnCallback, boolean invertY) {
        EntitySpawnContext spawnContext = this.createSpawnContext(context, initialPos);
        if (spawnContext == null) {
            return null;
        }

        ActionContext spawnActionContext = context.extend()
            .add(ItematicContextParameters.INTERACTED_POSITION, initialPos)
            .build();
        if (!this.applyRules(spawnActionContext, spawnContext)) {
            return null;
        }

        return this.spawn(spawnContext, spawnActionContext, spawnReason, spawnCallback, invertY);
    }

    @Nullable
    private EntitySpawnContext createSpawnContext(ActionContext context, Vec3 initialPos) {
        if (!(context.world() instanceof ServerLevel level)) {
            return null;
        }

        EntityType<?> type = this.entityType(context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY));
        if (!type.isAllowedInPeaceful() && level.getDifficulty() == Difficulty.PEACEFUL) {
            return null;
        }

        return new EntitySpawnContext(
            level,
            type,
            context.get(LootContextParams.THIS_ENTITY),
            initialPos
        );
    }

    private boolean applyRules(ActionContext spawnActionContext, EntitySpawnContext spawnContext) {
        LootContext predicateContext = Objects.requireNonNull(spawnActionContext.lootContext());
        for (ConditionedEntitySpawnRule spawnRule : this.spawnRules) {
            if (!spawnRule.apply(predicateContext, spawnContext)) {
                return false;
            }
        }

        return true;
    }

    @Nullable
    private Entity spawn(EntitySpawnContext spawnContext, ActionContext spawnActionContext, EntitySpawnReason spawnReason, EntitySpawnCallback spawnCallback, boolean invertY) {
        Entity entity = this.createEntity(spawnContext, spawnActionContext, spawnReason, spawnCallback, invertY);
        if (entity == null) {
            return null;
        }

        ServerLevel level = spawnContext.level();
        level.addFreshEntityWithPassengers(entity);
        ActionContext spawnedActionContext = spawnActionContext.extend()
            .add(ItematicContextParameters.SPAWNED_ENTITY, entity)
            .add(ItematicContextParameters.SPAWNED_POSITION, entity.position())
            .build();
        this.spawned(entity, level, spawnedActionContext);
        return entity;
    }

    @Nullable
    private Entity createEntity(EntitySpawnContext spawnContext, ActionContext spawnActionContext, EntitySpawnReason spawnReason, @Nullable EntitySpawnCallback spawnCallback, boolean invertY) {
        Entity entity = spawnContext.entityType().itematic$create(
            spawnActionContext,
            spawnReason,
            BlockPos.containing(spawnContext.spawnPosition()),
            EntitySpawnCallback.combine(
                this::applyComponents,
                spawnCallback
            ),
            this.allowItemData,
            invertY
        );
        if (entity == null) {
            return null;
        }

        Vec3 spawnPosition = spawnContext.spawnPosition();
        entity.snapTo(
            spawnPosition,
            spawnContext.yaw(),
            0.0f
        );
        return entity;
    }

    private void spawned(Entity entity, Level level, ActionContext spawnedContext) {
        this.spawnSound.ifPresent(spawnSound -> level.itematic$playSound(
            null,
            entity.position(),
            spawnSound.value(),
            SoundSource.BLOCKS,
            0.75f,
            0.8f
        ));
        level.gameEvent(
            spawnedContext.get(LootContextParams.THIS_ENTITY),
            GameEvent.ENTITY_PLACE,
            entity.position()
        );
        spawnedContext.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY)
            .itematic$invokeEvent(ItemEvents.SPAWN_ENTITY, spawnedContext);
    }

    private void applyComponents(Entity entity, ItemStack stack) {
        ((EntityAccessor) entity).itematic$copyComponentsFrom(
            PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, this.components)
        );
    }

    public static class Builder {
        private final Holder<EntityType<?>> entity;
        private final List<ConditionedEntitySpawnRule> spawnRules = new ArrayList<>();
        private DataComponentPatch components = DataComponentPatch.EMPTY;
        @Nullable
        private Holder<SoundEvent> spawnSound;
        private boolean allowItemData;

        private Builder(Holder<EntityType<?>> entity) {
            this.entity = entity;
        }

        public EntitySpawner build() {
            return new EntitySpawner(
                this.entity,
                this.spawnRules,
                this.components,
                Optional.ofNullable(this.spawnSound),
                this.allowItemData
            );
        }

        public Builder spawnRule(EntitySpawnRule<?> rule) {
            this.spawnRules.add(ConditionedEntitySpawnRule.of(rule));
            return this;
        }

        public Builder spawnRule(EntitySpawnRule<?> rule, LootItemCondition.Builder condition) {
            this.spawnRules.add(ConditionedEntitySpawnRule.of(rule, condition.build()));
            return this;
        }

        public Builder components(DataComponentPatch components) {
            this.components = components;
            return this;
        }

        public Builder spawnSound(Holder<SoundEvent> spawnSound) {
            this.spawnSound = spawnSound;
            return this;
        }

        public Builder allowItemData() {
            this.allowItemData = true;
            return this;
        }
    }
}
