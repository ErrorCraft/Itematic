package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class SmallFireballEntityInitializer implements EntityInitializer<SmallFireballEntity> {
    public static final SmallFireballEntityInitializer INSTANCE = new SmallFireballEntityInitializer();
    public static final MapCodec<SmallFireballEntityInitializer> CODEC = MapCodec.unit(INSTANCE);
    private static final double VELOCITY_DEVIATION = 0.11485d;

    private SmallFireballEntityInitializer() {}

    @Override
    public EntityType<?> type() {
        return EntityType.SMALL_FIREBALL;
    }

    @Override
    public SmallFireballEntity create(NewActionContext context, SpawnReason reason) {
        ServerWorld world = context.world();
        Random random = world.getRandom();
        Direction direction = context.getOrDefault(ItematicContextParameters.SIDE, Direction.UP);
        double velocityX = random.nextTriangular(direction.getOffsetX(), VELOCITY_DEVIATION);
        double velocityY = random.nextTriangular(direction.getOffsetY(), VELOCITY_DEVIATION);
        double velocityZ = random.nextTriangular(direction.getOffsetZ(), VELOCITY_DEVIATION);
        if (context.get(LootContextParameters.THIS_ENTITY) instanceof LivingEntity owner) {
            return new SmallFireballEntity(world, owner, new Vec3d(velocityX, velocityY, velocityZ));
        }

        Vec3d pos = context.get(ItematicContextParameters.INTERACTED_POSITION);
        if (pos == null) {
            return null;
        }

        return new SmallFireballEntity(world, pos.getX(), pos.getY(), pos.getZ(), new Vec3d(velocityX, velocityY, velocityZ));
    }
}
