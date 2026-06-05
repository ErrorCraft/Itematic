package net.errorcraft.itematic.item.placement;

import net.errorcraft.itematic.entity.EntitySpawnCallback;
import net.errorcraft.itematic.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.entity.spawn.rule.ConditionedEntitySpawnRule;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EntityPlacer<T extends Entity> {
    private final EntityType<T> type;
    private final List<ConditionedEntitySpawnRule> spawnRules;
    private final Optional<RegistryEntry<SoundEvent>> spawnSound;
    private final EntitySpawnCallback<T> spawnCallback;
    private final boolean allowItemData;

    private EntityPlacer(EntityType<T> type, List<ConditionedEntitySpawnRule> spawnRules, Optional<RegistryEntry<SoundEvent>> spawnSound, EntitySpawnCallback<T> spawnCallback, boolean allowItemData) {
        this.type = type;
        this.spawnRules = spawnRules;
        this.spawnSound = spawnSound;
        this.spawnCallback = spawnCallback;
        this.allowItemData = allowItemData;
    }

    public static <T extends Entity> EntityPlacer<T> of(EntityType<T> type, List<ConditionedEntitySpawnRule> spawnRules, Optional<RegistryEntry<SoundEvent>> spawnSound, EntitySpawnCallback<T> spawnCallback, boolean allowItemData) {
        return new EntityPlacer<>(type, spawnRules, spawnSound, spawnCallback, allowItemData);
    }

    public T place(ActionContext context, PositionTarget position, SpawnReason spawnReason) {
        if (!(context.world() instanceof ServerWorld world)) {
            return null;
        }

        BlockPos pos = context.get(position.parameter(), BlockPos::ofFloored);
        if (pos == null) {
            return null;
        }

        BlockState state = world.getBlockState(pos);
        Direction side = context.get(ItematicContextParameters.SIDE);
        BlockPos truePos = state.getCollisionShape(world, pos).isEmpty() || side == null
            ? pos
            : pos.offset(side);
        EntitySpawnContext spawnContext = new EntitySpawnContext(
            world,
            this.type,
            context.get(LootContextParameters.THIS_ENTITY),
            truePos
        );
        ActionContext spawnActionContext = context.extend()
            .add(ItematicContextParameters.INTERACTED_POSITION, truePos.toCenterPos())
            .build();
        return this.spawn(
            world,
            spawnActionContext,
            spawnContext,
            !Objects.equals(pos, truePos) && side == Direction.UP,
            spawnReason
        );
    }

    private T spawn(ServerWorld world, ActionContext spawnActionContext, EntitySpawnContext spawnContext, boolean invertY, SpawnReason spawnReason) {
        if (!this.maySpawn(spawnActionContext, spawnContext)) {
            return null;
        }

        T entity = this.type.itematic$create(
            spawnActionContext,
            spawnReason,
            BlockPos.ofFloored(spawnContext.spawnPosition()),
            this.spawnCallback,
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
        world.spawnEntityAndPassengers(entity);
        ActionContext spawnedActionContext = spawnActionContext.extend()
            .add(ItematicContextParameters.SPAWNED_ENTITY, entity)
            .add(ItematicContextParameters.SPAWNED_POSITION, spawnPosition)
            .build();
        this.spawned(entity, spawnPosition, world, spawnedActionContext);
        return entity;
    }

    private boolean maySpawn(ActionContext spawnActionContext, EntitySpawnContext spawnContext) {
        LootContext predicateContext = spawnActionContext.lootContext();
        for (ConditionedEntitySpawnRule spawnRule : this.spawnRules) {
            if (!spawnRule.apply(predicateContext, spawnContext)) {
                return false;
            }
        }

        return true;
    }

    private void spawned(Entity entity, Vec3d spawnPosition, World world, ActionContext spawnedContext) {
        this.spawnSound.ifPresent(spawnSound -> world.playSound(
            null,
            spawnPosition.getX(),
            spawnPosition.getY(),
            spawnPosition.getZ(),
            spawnSound.value(),
            SoundCategory.BLOCKS,
            0.75f,
            0.8f
        ));
        world.emitGameEvent(
            spawnedContext.get(LootContextParameters.THIS_ENTITY),
            GameEvent.ENTITY_PLACE,
            entity.getBlockPos()
        );
        spawnedContext.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY)
            .itematic$invokeEvent(ItemEvents.SPAWN_ENTITY, spawnedContext);
    }
}
