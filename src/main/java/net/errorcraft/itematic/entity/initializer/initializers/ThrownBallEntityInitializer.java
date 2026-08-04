package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public record ThrownBallEntityInitializer<T extends AbstractHurtingProjectile>(OwnerCreator<T> ownerCreator, SimpleCreator<T> simpleCreator) implements EntityInitializer<T> {
    private static final double VELOCITY_DEVIATION = 0.11485d;

    public static <T extends AbstractHurtingProjectile> EntityInitializer<T> of(OwnerCreator<T> ownerCreator, SimpleCreator<T> simpleCreator) {
        return new ThrownBallEntityInitializer<>(ownerCreator, simpleCreator);
    }

    @Override
    public T create(ActionContext context, EntitySpawnReason reason) {
        Level world = context.world();
        Player user = context.get(LootContextParams.THIS_ENTITY, Player.class);
        if (user != null) {
            return this.spawnFromUser(world, user);
        }

        Direction side = context.get(ItematicContextParameters.SIDE);
        Vec3 position = context.get(ItematicContextParameters.INTERACTED_POSITION);
        if (side != null && position != null) {
            return this.spawnFromSide(world, side, position);
        }

        return null;
    }

    private T spawnFromUser(Level world, Player user) {
        Vec3 position = user.getEyePosition().add(user.getForward().scale(0.8f));
        if (!world.getBlockState(BlockPos.containing(position)).canBeReplaced()) {
            position = user.getEyePosition().add(user.getForward().scale(0.05f));
        }

        return this.ownerCreator.create(
            user,
            world,
            position.x(),
            position.y(),
            position.z()
        );
    }

    private T spawnFromSide(Level world, Direction direction, Vec3 position) {
        RandomSource random = world.getRandom();
        double velocityX = random.triangle(direction.getStepX(), VELOCITY_DEVIATION);
        double velocityY = random.triangle(direction.getStepY(), VELOCITY_DEVIATION);
        double velocityZ = random.triangle(direction.getStepZ(), VELOCITY_DEVIATION);
        return this.simpleCreator.create(
            world,
            position.x(),
            position.y(),
            position.z(),
            new Vec3(velocityX, velocityY, velocityZ)
        );
    }

    @FunctionalInterface
    public interface OwnerCreator<T extends AbstractHurtingProjectile> {
        T create(Player player, Level world, double x, double y, double z);
    }

    @FunctionalInterface
    public interface SimpleCreator<T extends AbstractHurtingProjectile> {
        T create(Level world, double x, double y, double z, Vec3 velocity);
    }
}
