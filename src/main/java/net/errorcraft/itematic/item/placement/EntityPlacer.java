package net.errorcraft.itematic.item.placement;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BucketItemComponent;
import net.errorcraft.itematic.item.component.components.EntityItemComponent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
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
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    private final Hand hand;

    private EntityPlacer(ItemStack stack, ItemStackExchanger stackExchanger, World world, BlockPos blockPos, BlockState blockState, PlayerEntity player, EntityInitializer<?> initializer, Direction direction, boolean mayModifyBlock, SpawnReason spawnReason, BiConsumer<Entity, ItemStack> spawnCallback, boolean allowItemData, @Nullable Hand hand) {
        super(stack, stackExchanger, world, blockPos, blockState, player);
        this.initializer = initializer;
        this.direction = direction;
        this.mayModifyBlock = mayModifyBlock;
        this.spawnReason = spawnReason;
        this.spawnCallback = spawnCallback;
        this.allowItemData = allowItemData;
        this.hand = hand;
    }

    public static EntityPlacer spawned(ItemUsageContext context, ItemStack stack, ItemStackExchanger stackExchanger, EntityItemComponent entityItemComponent) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        return new EntityPlacer(context.getStack(), stackExchanger, world, blockPos, world.getBlockState(blockPos), context.getPlayer(), entityItemComponent.getEntityInitializer(stack, world.getRegistryManager()), context.getSide(), true, SpawnReason.SPAWN_ITEM_USE, null, entityItemComponent.allowItemData(), context.getHand());
    }

    public static EntityPlacer action(NewActionContext context, PositionTarget position, EntityInitializer<?> entityInitializer) {
        ItemStack stack = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY);
        BlockPos pos = context.getBlockPos(position.parameter());
        if (pos == null) {
            return null;
        }

        return new EntityPlacer(
            stack,
            context.stackExchanger(),
            context.world(),
            pos,
            context.world().getBlockState(pos),
            context.get(LootContextParameters.THIS_ENTITY) instanceof PlayerEntity player ? player : null,
            entityInitializer,
            context.getOrDefault(ItematicContextParameters.SIDE, Direction.UP),
            false,
            SpawnReason.COMMAND,
            null,
            false,
            context.get(ItematicContextParameters.HAND)
        );
    }

    public static EntityPlacer bucket(ItemStack stack, ItemStackExchanger stackExchanger, World world, BlockHitResult result, PlayerEntity player, EntityInitializer<?> initializer, Hand hand) {
        BlockPos blockPos = result.getBlockPos();
        return new EntityPlacer(stack, stackExchanger, world, blockPos, world.getBlockState(blockPos), player, initializer, result.getSide(), false, SpawnReason.BUCKET, BucketItemComponent::initializeBucketEntity, true, hand);
    }

    @Override
    public ItemResult place() {
        if (!this.mayModifyBlock || !this.tryModifySpawnerBlock()) {
            this.placeEntity();
        }
        return ItemResult.CONSUME;
    }

    private boolean tryModifySpawnerBlock() {
        if (!this.stack.itematic$hasBehavior(ItemComponentTypes.SPAWN_EGG)) {
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
        NewActionContext.Builder contextBuilder = this.addStackConsumer(NewActionContext.builder(serverWorld))
            .addOptional(LootContextParameters.THIS_ENTITY, this.player)
            .addOptional(LootContextParameters.ORIGIN, this.player, Entity::getPos)
            .add(ItematicContextParameters.INTERACTED_POSITION, offset.toCenterPos())
            .add(ItematicContextParameters.SIDE, this.direction)
            .add(LootContextParameters.TOOL, this.stack)
            .addOptional(ItematicContextParameters.HAND, this.hand);
        Entity entity = this.createEntity(offset, contextBuilder.build());
        if (entity == null) {
            return;
        }

        if (this.spawnCallback != null) {
            this.spawnCallback.accept(entity, this.stack);
        }

        this.tryDecrementStack();
        this.world.emitGameEvent(this.player, GameEvent.ENTITY_PLACE, entity.getBlockPos());

        contextBuilder.add(ItematicContextParameters.TARGET_ENTITY, entity);
        this.stack.itematic$invokeEvent(ItemEvents.SPAWN_ENTITY, contextBuilder.build());
    }

    private Entity createEntity(BlockPos offset, NewActionContext context) {
        if (this.world.isClient()) {
            return null;
        }

        if (this.allowItemData) {
            this.initializer.type().itematic$setInitializer(this.initializer, context);
            Entity entity = this.initializer.type().spawnFromItemStack((ServerWorld) this.world, this.stack, this.player, offset, this.spawnReason, true, !Objects.equals(this.blockPos, offset) && this.direction == Direction.UP);
            this.initializer.type().itematic$setInitializer(null, null);
            return entity;
        }

        Entity entity = this.initializer.create(context, this.spawnReason);
        if (entity != null) {
            entity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(offset));
            ((ServerWorld) this.world).spawnEntityAndPassengers(entity);
        }
        return entity;
    }

    private NewActionContext.Builder addStackConsumer(NewActionContext.Builder builder) {
        if (this.player != null) {
            return builder.stackExchanger(this.player, this.stack);
        }

        return builder.stackExchanger(this.direction, this.blockPos.toCenterPos(), this.stack);
    }
}
