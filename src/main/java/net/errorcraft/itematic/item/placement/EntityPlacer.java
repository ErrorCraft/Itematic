package net.errorcraft.itematic.item.placement;

import net.errorcraft.itematic.entity.EntitySpawnCallback;
import net.errorcraft.itematic.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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

    public Entity place(ActionContext context, PositionTarget position, SpawnReason spawnReason) {
        World world = context.world();
        if (world.isClient()) {
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
        return this.spawner.spawn(
            context,
            Vec3d.ofBottomCenter(truePos),
            spawnReason,
            this.spawnCallback,
            !Objects.equals(pos, truePos) && side == Direction.UP
        );
    }
}
