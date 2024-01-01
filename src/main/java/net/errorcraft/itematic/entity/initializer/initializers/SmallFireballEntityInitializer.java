package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public record SmallFireballEntityInitializer() implements EntityInitializer<SmallFireballEntity> {
    public static final SmallFireballEntityInitializer INSTANCE = new SmallFireballEntityInitializer();
    public static final Codec<SmallFireballEntityInitializer> CODEC = Codec.unit(INSTANCE);
    private static final double VELOCITY_DEVIATION = 0.11485d;

    @Override
    public EntityType<?> type() {
        return EntityType.SMALL_FIREBALL;
    }

    @Override
    public SmallFireballEntity create(ActionContext context) {
        ServerWorld world = context.world();
        Random random = world.getRandom();
        Direction direction = context.side();
        double velocityX = random.nextTriangular(direction.getOffsetX(), VELOCITY_DEVIATION);
        double velocityY = random.nextTriangular(direction.getOffsetX(), VELOCITY_DEVIATION);
        double velocityZ = random.nextTriangular(direction.getOffsetX(), VELOCITY_DEVIATION);
        if (context.entity(ActionContextParameter.THIS).orElse(null) instanceof LivingEntity owner) {
            return new SmallFireballEntity(world, owner, velocityX, velocityY, velocityZ);
        }
        Vec3d pos = context.position(ActionContextParameter.TARGET);
        return new SmallFireballEntity(world, pos.getX(), pos.getY(), pos.getZ(), velocityX, velocityY, velocityZ);
    }
}
