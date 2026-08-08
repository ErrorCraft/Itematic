package net.errorcraft.itematic.world.entity.spawn;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntitySpawnContext {
    private final ServerLevel level;
    private final EntityType<?> entityType;
    @Nullable
    private final Entity user;
    private Vec3 spawnPosition;
    private float yaw = 0.0f;

    public EntitySpawnContext(ServerLevel level, EntityType<?> entityType, @Nullable Entity user, Vec3 spawnPosition) {
        this.level = level;
        this.entityType = entityType;
        this.user = user;
        this.spawnPosition = spawnPosition;
    }

    public ServerLevel level() {
        return this.level;
    }

    public EntityType<?> entityType() {
        return this.entityType;
    }

    public Vec3 spawnPosition() {
        return this.spawnPosition;
    }

    public void spawnPosition(Vec3 spawnPosition) {
        this.spawnPosition = spawnPosition;
    }

    public float userAngle() {
        if (this.user == null) {
            return 0.0f;
        }

        return this.user.getYRot();
    }

    public float yaw() {
        return this.yaw;
    }

    public void yaw(float yaw) {
        this.yaw = yaw;
    }
}
