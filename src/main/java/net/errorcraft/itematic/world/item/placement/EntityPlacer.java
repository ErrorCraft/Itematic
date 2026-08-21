package net.errorcraft.itematic.world.item.placement;

import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.entity.EntitySpawnCallback;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class EntityPlacer {
    private final EntitySpawner spawner;
    @Nullable
    private final EntitySpawnCallback spawnCallback;

    private EntityPlacer(EntitySpawner spawner, @Nullable EntitySpawnCallback spawnCallback) {
        this.spawner = spawner;
        this.spawnCallback = spawnCallback;
    }

    public static EntityPlacer of(EntitySpawner entity, @Nullable EntitySpawnCallback spawnCallback) {
        return new EntityPlacer(entity, spawnCallback);
    }

    @Nullable
    public Entity place(ActionContext context, PositionTarget position, EntitySpawnReason spawnReason) {
        Level level = context.level();
        if (level.isClientSide()) {
            return null;
        }

        BlockPos pos = context.get(position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return null;
        }

        BlockState state = level.getBlockState(pos);
        Direction side = context.get(ItematicContextKeys.SIDE);
        BlockPos truePos = state.getCollisionShape(level, pos).isEmpty() || side == null
            ? pos
            : pos.relative(side);
        return this.spawner.spawn(
            context,
            Vec3.atBottomCenterOf(truePos),
            spawnReason,
            this.spawnCallback,
            !Objects.equals(pos, truePos) && side == Direction.UP
        );
    }
}
