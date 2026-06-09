package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public record ThrownBallEntityInitializer<T extends ExplosiveProjectileEntity>(OwnerCreator<T> ownerCreator, SimpleCreator<T> simpleCreator) implements EntityInitializer<T> {
    private static final double VELOCITY_DEVIATION = 0.11485d;

    public static <T extends ExplosiveProjectileEntity> EntityInitializer<T> of(OwnerCreator<T> ownerCreator, SimpleCreator<T> simpleCreator) {
        return new ThrownBallEntityInitializer<>(ownerCreator, simpleCreator);
    }

    @Override
    public T create(ActionContext context, SpawnReason reason) {
        World world = context.world();
        PlayerEntity user = context.get(LootContextParameters.THIS_ENTITY, PlayerEntity.class);
        if (user != null) {
            return this.spawnFromUser(world, user);
        }

        Direction side = context.get(ItematicContextParameters.SIDE);
        Vec3d position = context.get(ItematicContextParameters.INTERACTED_POSITION);
        if (side != null && position != null) {
            return this.spawnFromSide(world, side, position);
        }

        return null;
    }

    private T spawnFromUser(World world, PlayerEntity user) {
        Vec3d position = user.getEyePos().add(user.getRotationVecClient().multiply(0.8f));
        if (!world.getBlockState(BlockPos.ofFloored(position)).isReplaceable()) {
            position = user.getEyePos().add(user.getRotationVecClient().multiply(0.05f));
        }

        return this.ownerCreator.create(
            user,
            world,
            position.getX(),
            position.getY(),
            position.getZ()
        );
    }

    private T spawnFromSide(World world, Direction direction, Vec3d position) {
        Random random = world.getRandom();
        double velocityX = random.nextTriangular(direction.getOffsetX(), VELOCITY_DEVIATION);
        double velocityY = random.nextTriangular(direction.getOffsetY(), VELOCITY_DEVIATION);
        double velocityZ = random.nextTriangular(direction.getOffsetZ(), VELOCITY_DEVIATION);
        return this.simpleCreator.create(
            world,
            position.getX(),
            position.getY(),
            position.getZ(),
            new Vec3d(velocityX, velocityY, velocityZ)
        );
    }

    @FunctionalInterface
    public interface OwnerCreator<T extends ExplosiveProjectileEntity> {
        T create(PlayerEntity player, World world, double x, double y, double z);
    }

    @FunctionalInterface
    public interface SimpleCreator<T extends ExplosiveProjectileEntity> {
        T create(World world, double x, double y, double z, Vec3d velocity);
    }
}
