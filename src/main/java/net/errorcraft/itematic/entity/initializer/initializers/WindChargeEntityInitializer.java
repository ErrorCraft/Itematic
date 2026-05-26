package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WindChargeEntityInitializer implements EntityInitializer<WindChargeEntity> {
    public static final WindChargeEntityInitializer INSTANCE = new WindChargeEntityInitializer();
    private static final double VELOCITY_DEVIATION = 0.11485d;

    private WindChargeEntityInitializer() {}

    @Override
    public WindChargeEntity create(ActionContext context, SpawnReason reason) {
        World world = context.world();
        PlayerEntity user = context.get(LootContextParameters.THIS_ENTITY, PlayerEntity.class);
        if (user != null) {
            return spawnFromUser(world, user);
        }

        Direction side = context.get(ItematicContextParameters.SIDE);
        Vec3d position = context.get(ItematicContextParameters.INTERACTED_POSITION);
        if (side != null && position != null) {
            return spawnFromSide(world, side, position);
        }

        return null;
    }

    private static WindChargeEntity spawnFromUser(World world, PlayerEntity user) {
        Vec3d position = user.getEyePos().add(user.getRotationVecClient().multiply(0.8f));
        if (!world.getBlockState(BlockPos.ofFloored(position)).isReplaceable()) {
            position = user.getEyePos().add(user.getRotationVecClient().multiply(0.05f));
        }

        return new WindChargeEntity(user, world, position.getX(), position.getY(), position.getZ());
    }

    private static WindChargeEntity spawnFromSide(World world, Direction direction, Vec3d position) {
        Random random = world.getRandom();
        double velocityX = random.nextTriangular(direction.getOffsetX(), VELOCITY_DEVIATION);
        double velocityY = random.nextTriangular(direction.getOffsetY(), VELOCITY_DEVIATION);
        double velocityZ = random.nextTriangular(direction.getOffsetZ(), VELOCITY_DEVIATION);
        return new WindChargeEntity(world, position.getX(), position.getY(), position.getZ(), new Vec3d(velocityX, velocityY, velocityZ));
    }
}
