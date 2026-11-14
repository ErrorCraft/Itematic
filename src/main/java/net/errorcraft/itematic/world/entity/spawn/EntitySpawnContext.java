package net.errorcraft.itematic.world.entity.spawn;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.function.UnaryOperator;

public class EntitySpawnContext {
    private final ServerLevel level;
    private final EntityType<?> entityType;
    @Nullable
    private final Entity user;
    private Vec3 spawnPosition;
    private final Vec3 interactedPosition;
    private float yaw = 0.0f;
    @Nullable
    private final Direction interactedSide;

    public EntitySpawnContext(ServerLevel level, EntityType<?> entityType, @Nullable Entity user, Vec3 spawnPosition, @Nullable Vec3 interactedPosition, @Nullable Direction interactedSide) {
        this.level = level;
        this.entityType = entityType;
        this.user = user;
        this.spawnPosition = spawnPosition;
        this.interactedPosition = interactedPosition != null ? interactedPosition : spawnPosition;
        this.interactedSide = interactedSide;
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

    public void spawnPosition(UnaryOperator<Vec3> spawnPosition) {
        this.spawnPosition = spawnPosition.apply(this.spawnPosition);
    }

    public Vec3 interactedPosition() {
        return this.interactedPosition;
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

    @Nullable
    public Direction interactedSide() {
        return this.interactedSide;
    }
}
