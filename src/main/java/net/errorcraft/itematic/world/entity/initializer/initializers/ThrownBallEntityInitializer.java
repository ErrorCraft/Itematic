package net.errorcraft.itematic.world.entity.initializer.initializers;

import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record ThrownBallEntityInitializer<T extends AbstractHurtingProjectile>(OwnerCreator<T> ownerCreator, SimpleCreator<T> simpleCreator) implements EntityInitializer<T> {
    private static final double VELOCITY_DEVIATION = 0.11485d;

    public static <T extends AbstractHurtingProjectile> EntityInitializer<T> of(OwnerCreator<T> ownerCreator, SimpleCreator<T> simpleCreator) {
        return new ThrownBallEntityInitializer<>(ownerCreator, simpleCreator);
    }

    @Override
    public @Nullable T create(ActionContext context, EntitySpawnReason reason) {
        Level level = context.level();
        Player user = context.get(LootContextParams.THIS_ENTITY, Player.class);
        if (user != null) {
            return this.spawnFromUser(level, user);
        }

        Direction side = context.get(ItematicContextKeys.SIDE);
        Vec3 position = context.get(ItematicContextKeys.INTERACTED_POSITION);
        if (side != null && position != null) {
            return this.spawnFromSide(level, side, position);
        }

        return null;
    }

    private T spawnFromUser(Level level, Player user) {
        Vec3 position = user.getEyePosition().add(user.getForward().scale(0.8f));
        if (!level.getBlockState(BlockPos.containing(position)).canBeReplaced()) {
            position = user.getEyePosition().add(user.getForward().scale(0.05f));
        }

        return this.ownerCreator.create(
            user,
            level,
            position.x(),
            position.y(),
            position.z()
        );
    }

    private T spawnFromSide(Level level, Direction direction, Vec3 position) {
        RandomSource random = level.getRandom();
        double velocityX = random.triangle(direction.getStepX(), VELOCITY_DEVIATION);
        double velocityY = random.triangle(direction.getStepY(), VELOCITY_DEVIATION);
        double velocityZ = random.triangle(direction.getStepZ(), VELOCITY_DEVIATION);
        return this.simpleCreator.create(
            level,
            position.x(),
            position.y(),
            position.z(),
            new Vec3(velocityX, velocityY, velocityZ)
        );
    }

    @FunctionalInterface
    public interface OwnerCreator<T extends AbstractHurtingProjectile> {
        T create(Player player, Level levels, double x, double y, double z);
    }

    @FunctionalInterface
    public interface SimpleCreator<T extends AbstractHurtingProjectile> {
        T create(Level level, double x, double y, double z, Vec3 velocity);
    }
}
