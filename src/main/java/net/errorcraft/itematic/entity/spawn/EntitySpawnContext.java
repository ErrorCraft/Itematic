package net.errorcraft.itematic.entity.spawn;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class EntitySpawnContext {
    private final ServerWorld world;
    private final EntityType<?> entityType;
    @Nullable
    private final Entity user;
    private Vec3d spawnPosition;
    private float yaw = 0.0f;

    public EntitySpawnContext(ServerWorld world, EntityType<?> entityType, @Nullable Entity user, BlockPos spawnPosition) {
        this.world = world;
        this.entityType = entityType;
        this.user = user;
        this.spawnPosition = Vec3d.ofBottomCenter(spawnPosition);
    }

    public ServerWorld world() {
        return this.world;
    }

    public EntityType<?> entityType() {
        return this.entityType;
    }

    public Vec3d spawnPosition() {
        return this.spawnPosition;
    }

    public void spawnPosition(Vec3d spawnPosition) {
        this.spawnPosition = spawnPosition;
    }

    public float userAngle() {
        if (this.user == null) {
            return 0.0f;
        }

        return this.user.getYaw();
    }

    public float yaw() {
        return this.yaw;
    }

    public void yaw(float yaw) {
        this.yaw = yaw;
    }
}
