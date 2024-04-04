package net.errorcraft.itematic.item.placement;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BucketItemComponent;
import net.errorcraft.itematic.item.component.components.EntityItemComponent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
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

    public EntityPlacer(ItemStack stack, ItemStackConsumer resultStackConsumer, World world, BlockPos blockPos, BlockState blockState, PlayerEntity player, EntityInitializer<?> initializer, Direction direction, boolean mayModifyBlock, SpawnReason spawnReason, BiConsumer<Entity, ItemStack> spawnCallback, boolean allowItemData, Hand hand) {
        super(stack, resultStackConsumer, world, blockPos, blockState, player);
        this.initializer = initializer;
        this.direction = direction;
        this.mayModifyBlock = mayModifyBlock;
        this.spawnReason = spawnReason;
        this.spawnCallback = spawnCallback;
        this.allowItemData = allowItemData;
        this.hand = hand;
    }

    public static EntityPlacer spawned(ItemUsageContext context, ItemStack stack, ItemStackConsumer resultStackConsumer, EntityItemComponent entityItemComponent) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        return new EntityPlacer(context.getStack(), resultStackConsumer, world, blockPos, world.getBlockState(blockPos), context.getPlayer(), entityItemComponent.getEntityInitializer(stack), context.getSide(), true, SpawnReason.SPAWN_EGG, null, entityItemComponent.allowItemData(), context.getHand());
    }

    public static EntityPlacer action(ActionContext context, ActionContextParameter position, EntityItemComponent entityItemComponent) {
        ItemStack stack = context.stack();
        BlockPos pos = context.blockPos(position);
        return new EntityPlacer(stack, context.resultStackConsumer(), context.world(), pos, context.world().getBlockState(pos), context.player(ActionContextParameter.THIS).orElse(null), entityItemComponent.getEntityInitializer(stack), context.side(), false, SpawnReason.COMMAND, null, entityItemComponent.allowItemData(), context.hand());
    }

    public static EntityPlacer action(ActionContext context, ActionContextParameter position, EntityInitializer<?> entityInitializer) {
        ItemStack stack = context.stack();
        BlockPos pos = context.blockPos(position);
        return new EntityPlacer(stack, context.resultStackConsumer(), context.world(), pos, context.world().getBlockState(pos), context.player(ActionContextParameter.THIS).orElse(null), entityInitializer, context.side(), false, SpawnReason.COMMAND, null, false, context.hand());
    }

    public static EntityPlacer bucket(ItemStack stack, ItemStackConsumer resultStackConsumer, World world, BlockHitResult result, PlayerEntity player, EntityInitializer<?> initializer, Hand hand) {
        BlockPos blockPos = result.getBlockPos();
        return new EntityPlacer(stack, resultStackConsumer, world, blockPos, world.getBlockState(blockPos), player, initializer, result.getSide(), false, SpawnReason.BUCKET, BucketItemComponent::initializeBucketEntity, true, hand);
    }

    @Override
    public ActionResult place() {
        if (!this.mayModifyBlock || !this.tryModifySpawnerBlock()) {
            this.placeEntity();
        }
        return ActionResult.CONSUME;
    }

    private boolean tryModifySpawnerBlock() {
        if (!this.stack.itematic$hasComponent(ItemComponentTypes.SPAWN_EGG)) {
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
        this.tryDecrementStack();
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
        ActionContext context = ActionContext.builder(serverWorld, this.stack, this.resultStackConsumer, this.hand)
            .entityPosition(ActionContextParameter.THIS, this.player)
            .position(ActionContextParameter.TARGET, offset)
            .side(this.direction)
            .build();
        Entity entity = this.createEntity(offset, context);
        if (entity == null) {
            return;
        }
        if (this.spawnCallback != null) {
            this.spawnCallback.accept(entity, this.stack);
        }
        this.tryDecrementStack();
        this.world.emitGameEvent(this.player, GameEvent.ENTITY_PLACE, entity.getBlockPos());
        this.stack.itematic$invokeEvent(ItemEvents.SPAWN_ENTITY, context.builderForCopy().entity(ActionContextParameter.TARGET, entity).build());
    }

    private Entity createEntity(BlockPos offset, ActionContext context) {
        if (this.world.isClient()) {
            return null;
        }
        if (this.allowItemData) {
            this.initializer.type().itematic$setInitializer(this.initializer, context);
            Entity entity = this.initializer.type().spawnFromItemStack((ServerWorld) this.world, this.stack, this.player, offset, this.spawnReason, true, !Objects.equals(this.blockPos, offset) && this.direction == Direction.UP);
            this.initializer.type().itematic$setInitializer(null, null);
            return entity;
        }
        Entity entity = this.initializer.create(context);
        if (entity != null) {
            entity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(offset));
            ((ServerWorld) this.world).spawnEntityAndPassengers(entity);
        }
        return entity;
    }
}
