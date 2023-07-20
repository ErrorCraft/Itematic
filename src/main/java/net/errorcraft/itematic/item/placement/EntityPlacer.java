package net.errorcraft.itematic.item.placement;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BucketItemComponent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public class EntityPlacer extends Placer {
    private final EntityType<?> entityType;
    private final Direction direction;
    private final boolean mayModifyBlock;
    private final SpawnReason spawnReason;
    private final BiConsumer<Entity, ItemStack> spawnCallback;

    public EntityPlacer(ItemStack stack, World world, BlockPos blockPos, BlockState blockState, PlayerEntity player, EntityType<?> entityType, Direction direction, boolean mayModifyBlock, SpawnReason spawnReason, BiConsumer<Entity, ItemStack> spawnCallback) {
        super(stack, world, blockPos, blockState, player);
        this.entityType = entityType;
        this.direction = direction;
        this.mayModifyBlock = mayModifyBlock;
        this.spawnReason = spawnReason;
        this.spawnCallback = spawnCallback;
    }

    public static EntityPlacer spawned(ItemUsageContext context, EntityType<?> entityType) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        return new EntityPlacer(context.getStack(), world, blockPos, world.getBlockState(blockPos), context.getPlayer(), entityType, context.getSide(), true, SpawnReason.SPAWN_EGG, null);
    }

    public static EntityPlacer dispensed(BlockPointer pointer, ItemStack stack, EntityType<?> entityType, Direction direction) {
        return new EntityPlacer(stack, pointer.getWorld(), pointer.getPos(), pointer.getBlockState(), null, entityType, direction, false, SpawnReason.DISPENSER, null);
    }

    public static EntityPlacer bucket(ItemStack stack, World world, BlockHitResult result, PlayerEntity player, RegistryEntry<EntityType<?>> entityType) {
        BlockPos blockPos = result.getBlockPos();
        return new EntityPlacer(stack, world, blockPos, world.getBlockState(blockPos), player, entityType.value(), result.getSide(), false, SpawnReason.BUCKET, BucketItemComponent::initializeBucketEntity);
    }

    @Override
    public TypedActionResult<ItemStack> place() {
        if (!this.mayModifyBlock || !this.tryModifySpawnerBlock()) {
            this.placeEntity();
        }
        return TypedActionResult.consume(this.stack);
    }

    private boolean tryModifySpawnerBlock() {
        if (!this.stack.hasComponent(ItemComponentTypes.SPAWN_EGG)) {
            return false;
        }
        if (!this.blockState.isOf(Blocks.SPAWNER)) {
            return false;
        }
        Optional<MobSpawnerBlockEntity> optionalBlockEntity = this.world.getBlockEntity(this.blockPos, BlockEntityType.MOB_SPAWNER);
        if (optionalBlockEntity.isEmpty()) {
            return false;
        }
        this.modifySpawnerBlock(optionalBlockEntity.get());
        this.stack.decrement(1);
        return true;
    }

    private void modifySpawnerBlock(MobSpawnerBlockEntity blockEntity) {
        blockEntity.setEntityType(this.entityType, this.world.getRandom());
        blockEntity.markDirty();
        this.world.updateListeners(this.blockPos, this.blockState, this.blockState, Block.NOTIFY_ALL);
        this.world.emitGameEvent(this.player, GameEvent.BLOCK_CHANGE, this.blockPos);
    }

    private void placeEntity() {
        BlockPos offset = this.blockState.getCollisionShape(this.world, this.blockPos).isEmpty() ? this.blockPos : this.blockPos.offset(this.direction);
        Entity entity = this.createEntity(offset);
        if (entity == null) {
            return;
        }
        if (this.spawnCallback != null) {
            this.spawnCallback.accept(entity, this.stack);
        }
        this.stack.decrement(1);
        this.world.emitGameEvent(this.player, GameEvent.ENTITY_PLACE, this.blockPos);
        if (this.world instanceof ServerWorld serverWorld) {
            ActionContext.Builder builder = ActionContext.builder(serverWorld, this.stack)
                .entityPosition(ActionContextParameter.THIS, this.player)
                .position(ActionContextParameter.TARGET, offset.toCenterPos());
            stack.invokeEvent(ItemEvents.SPAWN_ENTITY, builder);
        }
    }

    private Entity createEntity(BlockPos offset) {
        if (this.world.isClient()) {
            return null;
        }
        return this.entityType.spawnFromItemStack((ServerWorld)this.world, this.stack, this.player, offset, this.spawnReason, true, !Objects.equals(this.blockPos, offset) && this.direction == Direction.UP);
    }
}
