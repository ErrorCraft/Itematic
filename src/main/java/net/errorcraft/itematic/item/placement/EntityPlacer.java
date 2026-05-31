package net.errorcraft.itematic.item.placement;

import net.errorcraft.itematic.entity.EntitySpawnCallback;
import net.errorcraft.itematic.entity.spawn.EntitySpawnContext;
import net.errorcraft.itematic.entity.spawn.rule.ConditionedEntitySpawnRule;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
    private final boolean mayModifyBlock;
    private final SpawnReason spawnReason;
    private final EntitySpawnCallback<T> spawnCallback;
    private final boolean allowItemData;
    private final PositionTarget position;
    private final ItemStack stack;

    private EntityPlacer(EntityType<T> type, List<ConditionedEntitySpawnRule> spawnRules, Optional<RegistryEntry<SoundEvent>> spawnSound, ActionContext context, boolean mayModifyBlock, SpawnReason spawnReason, EntitySpawnCallback<T> spawnCallback, boolean allowItemData, PositionTarget position) {
        this.type = type;
        this.spawnRules = spawnRules;
        this.spawnSound = spawnSound;
        this.context = context;
        this.mayModifyBlock = mayModifyBlock;
        this.spawnReason = spawnReason;
        this.spawnCallback = spawnCallback;
        this.allowItemData = allowItemData;
        this.position = position;
        this.stack = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY);
    }

    public static <T extends Entity> EntityPlacer<T> of(EntityType<T> type, List<ConditionedEntitySpawnRule> spawnRules, Optional<RegistryEntry<SoundEvent>> spawnSound, ActionContext context, boolean mayModifyBlock, SpawnReason spawnReason, EntitySpawnCallback<T> spawnCallback, boolean allowItemData, PositionTarget position) {
        return new EntityPlacer<>(type, spawnRules, spawnSound, context, mayModifyBlock, spawnReason, spawnCallback, allowItemData, position);
    }

    public T place() {
        BlockPos pos = this.context.getBlockPos(this.position.parameter());
        if (pos == null) {
            return null;
        }

        BlockState state = this.context.world().getBlockState(pos);
        if (this.mayModifyBlock && this.modifySpawnerBlock(pos, state)) {
            return null;
        }

        return this.spawn(pos, state);
    }

    private boolean modifySpawnerBlock(BlockPos pos, BlockState state) {
        if (!this.stack.itematic$hasBehavior(ItemComponentTypes.SPAWN_EGG)) {
            return false;
        }

        World world = this.context.world();
        if (!state.isOf(Blocks.SPAWNER)) {
            return false;
        }

        Optional<MobSpawnerBlockEntity> blockEntity = world.getBlockEntity(pos, BlockEntityType.MOB_SPAWNER);
        if (blockEntity.isEmpty()) {
            return false;
        }

        this.modifySpawnerBlock(world, blockEntity.get(), pos, state);
        this.decrementStack();
        return true;
    }

    private void modifySpawnerBlock(World world, MobSpawnerBlockEntity blockEntity, BlockPos pos, BlockState state) {
        blockEntity.setEntityType(this.type, world.getRandom());
        blockEntity.markDirty();
        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
        world.emitGameEvent(
            this.context.get(LootContextParameters.THIS_ENTITY),
            GameEvent.BLOCK_CHANGE,
            pos
        );
    }

    private T spawn(BlockPos pos, BlockState state) {
        if (!(this.context.world() instanceof ServerWorld world)) {
            return null;
        }

        Direction side = this.context.get(ItematicContextParameters.SIDE);
        BlockPos offset = state.getCollisionShape(world, pos).isEmpty() || side == null
            ? pos
            : pos.offset(side);
        EntitySpawnContext spawnContext = new EntitySpawnContext(
            world,
            this.type,
            this.context.get(LootContextParameters.THIS_ENTITY),
            offset
        );
        if (!this.maySpawn(spawnContext)) {
            return null;
        }

        T entity = this.spawn(
            world,
            BlockPos.ofFloored(spawnContext.spawnPosition()),
            !Objects.equals(pos, offset) && side == Direction.UP
        );
        if (entity == null) {
            return null;
        }

        Vec3d spawnPosition = spawnContext.spawnPosition();
        entity.refreshPositionAndAngles(
            spawnPosition.getX(),
            spawnPosition.getY(),
            spawnPosition.getZ(),
            spawnContext.yaw(),
            0.0f
        );
        this.spawned(entity, spawnPosition, world);
        return entity;
    }

    private boolean maySpawn(EntitySpawnContext spawnContext) {
        LootContext predicateContext = this.context.lootContext();
        for (ConditionedEntitySpawnRule spawnRule : this.spawnRules) {
            if (!spawnRule.apply(predicateContext, spawnContext)) {
                return false;
            }
        }

        return true;
    }

    private T spawn(ServerWorld world, BlockPos pos, boolean invertY) {
        T entity = this.type.itematic$create(
            this.context,
            this.spawnReason,
            pos,
            this.spawnCallback,
            this.allowItemData,
            invertY
        );
        if (entity == null) {
            return null;
        }

        world.spawnEntityAndPassengers(entity);
        return entity;
    }

    private void spawned(Entity entity, Vec3d spawnPosition, World world) {
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
        this.decrementStack();
        world.emitGameEvent(
            this.context.get(LootContextParameters.THIS_ENTITY),
            GameEvent.ENTITY_PLACE,
            entity.getBlockPos()
        );
        ActionContext extendedContext = this.context.extend()
            .add(ItematicContextParameters.TARGET_ENTITY, entity)
            .build();
        this.stack.itematic$invokeEvent(ItemEvents.SPAWN_ENTITY, extendedContext);
    }

    private void decrementStack() {
        this.stack.decrementUnlessCreative(
            1,
            this.context.get(LootContextParameters.THIS_ENTITY, LivingEntity.class)
        );
    }
}
