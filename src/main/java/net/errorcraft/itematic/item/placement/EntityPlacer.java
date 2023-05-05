package net.errorcraft.itematic.item.placement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Objects;
import java.util.Optional;

public class EntityPlacer extends Placer {
    private final EntityType<?> entityType;
    private final Direction direction;

    public EntityPlacer(ItemStack stack, World world, BlockPos blockPos, BlockState blockState, PlayerEntity player, EntityType<?> entityType, Direction direction) {
        super(stack, world, blockPos, blockState, player);
        this.entityType = entityType;
        this.direction = direction;
    }

    public static EntityPlacer of(ItemUsageContext context, EntityType<?> entityType) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        return new EntityPlacer(context.getStack(), world, blockPos, world.getBlockState(blockPos), context.getPlayer(), entityType, context.getSide());
    }

    @Override
    public TypedActionResult<ItemStack> place() {
        if (!this.tryModifySpawnerBlock()) {
            this.placeEntity();
        }
        return TypedActionResult.consume(this.stack);
    }

    private boolean tryModifySpawnerBlock() {
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
        BlockPos blockPos2 = this.blockState.getCollisionShape(world, blockPos).isEmpty() ? blockPos : blockPos.offset(direction);
        if (this.entityType.spawnFromItemStack((ServerWorld) this.world, this.stack, this.player, blockPos2, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockPos, blockPos2) && direction == Direction.UP) != null) {
            this.stack.decrement(1);
            world.emitGameEvent(player, GameEvent.ENTITY_PLACE, blockPos);
        }
    }
}
