package net.errorcraft.itematic.entity.spawn;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.EntitySpawnCallback;
import net.errorcraft.itematic.entity.spawn.rule.ConditionedEntitySpawnRule;
import net.errorcraft.itematic.entity.spawn.rule.EntitySpawnRule;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.mixin.entity.EntityAccessor;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record EntitySpawner(RegistryEntry<EntityType<?>> entity, List<ConditionedEntitySpawnRule> spawnRules, ComponentChanges components, Optional<RegistryEntry<SoundEvent>> spawnSound, boolean allowItemData) {
    public static final Codec<EntitySpawner> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Registries.ENTITY_TYPE.getEntryCodec().fieldOf("entity").forGetter(EntitySpawner::entity),
        ConditionedEntitySpawnRule.CODEC.listOf().optionalFieldOf("spawn_rules", List.of()).forGetter(EntitySpawner::spawnRules),
        ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter(EntitySpawner::components),
        SoundEvent.ENTRY_CODEC.optionalFieldOf("spawn_sound").forGetter(EntitySpawner::spawnSound),
        Codec.BOOL.optionalFieldOf("allow_item_data", false).forGetter(EntitySpawner::allowItemData)
    ).apply(instance, EntitySpawner::new));

    public static EntitySpawner of(RegistryEntry<EntityType<?>> entity) {
        return EntitySpawner.builder(entity).build();
    }

    public static Builder builder(RegistryEntry<EntityType<?>> entity) {
        return new Builder(entity);
    }

    public EntityType<?> entityType(ItemStack stack, RegistryWrapper.WrapperLookup registries) {
        if (!this.allowItemData) {
            return this.entity.value();
        }

        NbtComponent entityData = stack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
        EntityType<?> entityType = entityData.getRegistryValueOfId(registries, RegistryKeys.ENTITY_TYPE);
        if (entityType != null) {
            return entityType;
        }

        return this.entity.value();
    }

    public Entity spawn(ActionContext context, Vec3d initialPos, SpawnReason spawnReason, EntitySpawnCallback spawnCallback, boolean invertY) {
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

    private EntitySpawnContext createSpawnContext(ActionContext context, Vec3d initialPos) {
        if (!(context.world() instanceof ServerWorld world)) {
            return null;
        }

        EntityType<?> type = this.entityType(
            context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY),
            context.world().getRegistryManager()
        );
        return new EntitySpawnContext(
            world,
            type,
            context.get(LootContextParameters.THIS_ENTITY),
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

    private Entity spawn(EntitySpawnContext spawnContext, ActionContext spawnActionContext, SpawnReason spawnReason, EntitySpawnCallback spawnCallback, boolean invertY) {
        Entity entity = this.createEntity(spawnContext, spawnActionContext, spawnReason, spawnCallback, invertY);
        if (entity == null) {
            return null;
        }

        ServerWorld world = spawnContext.world();
        world.spawnEntityAndPassengers(entity);
        ActionContext spawnedActionContext = spawnActionContext.extend()
            .add(ItematicContextParameters.SPAWNED_ENTITY, entity)
            .add(ItematicContextParameters.SPAWNED_POSITION, entity.getPos())
            .build();
        this.spawned(entity, world, spawnedActionContext);
        return entity;
    }

    private Entity createEntity(EntitySpawnContext spawnContext, ActionContext spawnActionContext, SpawnReason spawnReason, @Nullable EntitySpawnCallback spawnCallback, boolean invertY) {
        Entity entity = spawnContext.entityType().itematic$create(
            spawnActionContext,
            spawnReason,
            BlockPos.ofFloored(spawnContext.spawnPosition()),
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

        Vec3d spawnPosition = spawnContext.spawnPosition();
        entity.refreshPositionAndAngles(
            spawnPosition,
            spawnContext.yaw(),
            0.0f
        );
        return entity;
    }

    private void spawned(Entity entity, World world, ActionContext spawnedContext) {
        this.spawnSound.ifPresent(spawnSound -> world.itematic$playSound(
            null,
            entity.getPos(),
            spawnSound.value(),
            SoundCategory.BLOCKS,
            0.75f,
            0.8f
        ));
        world.emitGameEvent(
            spawnedContext.get(LootContextParameters.THIS_ENTITY),
            GameEvent.ENTITY_PLACE,
            entity.getPos()
        );
        spawnedContext.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY)
            .itematic$invokeEvent(ItemEvents.SPAWN_ENTITY, spawnedContext);
    }

    private void applyComponents(Entity entity, ItemStack stack) {
        ((EntityAccessor) entity).itematic$copyComponentsFrom(
            MergedComponentMap.create(ComponentMap.EMPTY, this.components)
        );
    }

    public static class Builder {
        private final RegistryEntry<EntityType<?>> entity;
        private final List<ConditionedEntitySpawnRule> spawnRules = new ArrayList<>();
        private ComponentChanges components = ComponentChanges.EMPTY;
        private RegistryEntry<SoundEvent> spawnSound;
        private boolean allowItemData;

        private Builder(RegistryEntry<EntityType<?>> entity) {
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

        public Builder spawnRule(EntitySpawnRule<?> rule, LootCondition.Builder condition) {
            this.spawnRules.add(ConditionedEntitySpawnRule.of(rule, condition.build()));
            return this;
        }

        public Builder components(ComponentChanges components) {
            this.components = components;
            return this;
        }

        public Builder spawnSound(RegistryEntry<SoundEvent> spawnSound) {
            this.spawnSound = spawnSound;
            return this;
        }

        public Builder allowItemData(boolean allowItemData) {
            this.allowItemData = allowItemData;
            return this;
        }
    }
}
