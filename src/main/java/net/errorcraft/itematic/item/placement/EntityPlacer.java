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
    private final ActionContext context;
    private final SpawnReason spawnReason;
    private final EntitySpawnCallback<T> spawnCallback;
    private final boolean allowItemData;
    private final PositionTarget position;
    private final ItemStack stack;

    private EntityPlacer(EntityType<T> type, List<ConditionedEntitySpawnRule> spawnRules, Optional<RegistryEntry<SoundEvent>> spawnSound, ActionContext context, SpawnReason spawnReason, EntitySpawnCallback<T> spawnCallback, boolean allowItemData, PositionTarget position) {
        this.type = type;
        this.spawnRules = spawnRules;
        this.spawnSound = spawnSound;
        this.context = context;
        this.spawnReason = spawnReason;
        this.spawnCallback = spawnCallback;
        this.allowItemData = allowItemData;
        this.position = position;
        this.stack = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY);
    }

    public static <T extends Entity> EntityPlacer<T> of(EntityType<T> type, List<ConditionedEntitySpawnRule> spawnRules, Optional<RegistryEntry<SoundEvent>> spawnSound, ActionContext context, SpawnReason spawnReason, EntitySpawnCallback<T> spawnCallback, boolean allowItemData, PositionTarget position) {
        return new EntityPlacer<>(type, spawnRules, spawnSound, context, spawnReason, spawnCallback, allowItemData, position);
    }

    public T place() {
        if (!(this.context.world() instanceof ServerWorld world)) {
            return null;
        }

        BlockPos pos = this.context.getBlockPos(this.position.parameter());
        if (pos == null) {
            return null;
        }

        BlockState state = this.context.world().getBlockState(pos);
        Direction side = this.context.get(ItematicContextParameters.SIDE);
        BlockPos truePos = state.getCollisionShape(world, pos).isEmpty() || side == null
            ? pos
            : pos.offset(side);
        ActionContext spawnActionContext = this.context.extend()
            .add(ItematicContextParameters.INTERACTED_POSITION, truePos.toCenterPos())
            .build();
        EntitySpawnContext spawnContext = new EntitySpawnContext(
            world,
            this.type,
            this.context.get(LootContextParameters.THIS_ENTITY),
            truePos
        );
        return this.spawn(
            world,
            spawnActionContext,
            spawnContext,
            !Objects.equals(pos, truePos) && side == Direction.UP
        );
    }

    private T spawn(ServerWorld world, ActionContext spawnActionContext, EntitySpawnContext spawnContext, boolean invertY) {
        if (!this.maySpawn(spawnActionContext, spawnContext)) {
            return null;
        }

        T entity = this.type.itematic$create(
            spawnActionContext,
            this.spawnReason,
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
            this.context.get(LootContextParameters.THIS_ENTITY),
            GameEvent.ENTITY_PLACE,
            entity.getBlockPos()
        );
        this.stack.itematic$invokeEvent(ItemEvents.SPAWN_ENTITY, spawnedContext);
    }
}
