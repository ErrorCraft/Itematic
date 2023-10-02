package net.errorcraft.itematic.item.placement;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BucketItemComponent;
import net.errorcraft.itematic.item.component.components.EntityItemComponent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
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
    private final EntityInitializer<?> initializer;
    private final Direction direction;
    private final boolean mayModifyBlock;
    private final SpawnReason spawnReason;
    private final BiConsumer<Entity, ItemStack> spawnCallback;
    private final boolean allowItemData;
    private final Hand hand;

    public EntityPlacer(ItemStack stack, World world, BlockPos blockPos, BlockState blockState, PlayerEntity player, EntityInitializer<?> initializer, Direction direction, boolean mayModifyBlock, SpawnReason spawnReason, BiConsumer<Entity, ItemStack> spawnCallback, boolean allowItemData, Hand hand) {
        super(stack, world, blockPos, blockState, player);
        this.initializer = initializer;
        this.direction = direction;
        this.mayModifyBlock = mayModifyBlock;
        this.spawnReason = spawnReason;
        this.spawnCallback = spawnCallback;
        this.allowItemData = allowItemData;
        this.hand = hand;
    }

    public static EntityPlacer spawned(ItemUsageContext context, ItemStack stack, EntityItemComponent entityItemComponent) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        return new EntityPlacer(context.getStack(), world, blockPos, world.getBlockState(blockPos), context.getPlayer(), entityItemComponent.getEntityInitializer(stack), context.getSide(), true, SpawnReason.SPAWN_EGG, null, entityItemComponent.allowItemData(), context.getHand());
    }

    public static EntityPlacer dispensed(BlockPointer pointer, ItemStack stack, EntityItemComponent entityItemComponent) {
        BlockState state = pointer.state();
        return new EntityPlacer(stack, pointer.world(), pointer.pos(), state, null, entityItemComponent.getEntityInitializer(stack), state.get(DispenserBlock.FACING), false, SpawnReason.DISPENSER, null, entityItemComponent.allowItemData(), null);
    }

    public static EntityPlacer bucket(ItemStack stack, World world, BlockHitResult result, PlayerEntity player, EntityInitializer<?> initializer, Hand hand) {
        BlockPos blockPos = result.getBlockPos();
        return new EntityPlacer(stack, world, blockPos, world.getBlockState(blockPos), player, initializer, result.getSide(), false, SpawnReason.BUCKET, BucketItemComponent::initializeBucketEntity, true, hand);
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
        blockEntity.setEntityType(this.initializer.type(), this.world.getRandom());
        blockEntity.markDirty();
        this.world.updateListeners(this.blockPos, this.blockState, this.blockState, Block.NOTIFY_ALL);
        this.world.emitGameEvent(this.player, GameEvent.BLOCK_CHANGE, this.blockPos);
    }

    private void placeEntity() {
        if (!(this.world instanceof ServerWorld serverWorld)) {
            return;
        }
        BlockPos offset = this.blockState.getCollisionShape(this.world, this.blockPos).isEmpty() ? this.blockPos : this.blockPos.offset(this.direction);
        MutableActionContext context = MutableActionContext.stackUsage(serverWorld, this.stack, this.hand)
            .entityPosition(ActionContextParameter.THIS, this.player)
            .position(ActionContextParameter.TARGET, offset)
            .side(this.direction);
        Entity entity = this.createEntity(offset, context);
        if (entity == null) {
            return;
        }
        if (this.spawnCallback != null) {
            this.spawnCallback.accept(entity, this.stack);
        }
        this.stack.decrement(1);
        this.world.emitGameEvent(this.player, GameEvent.ENTITY_PLACE, entity.getBlockPos());
        this.stack.invokeEvent(ItemEvents.SPAWN_ENTITY, context.entity(ActionContextParameter.TARGET, entity));
    }

    private Entity createEntity(BlockPos offset, ActionContext context) {
        if (this.world.isClient()) {
            return null;
        }
        if (this.allowItemData) {
            this.initializer.type().setInitializer(this.initializer, this.direction);
            Entity entity = this.initializer.type().spawnFromItemStack((ServerWorld) this.world, this.stack, this.player, offset, this.spawnReason, true, !Objects.equals(this.blockPos, offset) && this.direction == Direction.UP);
            this.initializer.type().setInitializer(null, null);
            return entity;
        }
        Entity entity = this.initializer.create(context);
        if (entity != null) {
            ((ServerWorld) this.world).spawnEntityAndPassengers(entity);
        }
        return entity;
    }
}
