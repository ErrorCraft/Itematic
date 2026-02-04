package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.PointableItemComponent;
import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AngleModelOverride implements ModelOverride {
    private static final float INTERPOLATION_FACTOR = 0.8f;
    private static final double DISTANCE_EPSILON = 0.00001d;
    private static final Interpolator INTERPOLATOR = new Interpolator(INTERPOLATION_FACTOR);
    private static final Interpolator AIMLESS_INTERPOLATOR = new Interpolator(INTERPOLATION_FACTOR);

    @Override
    public float apply(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        if (world == null) {
            return 0.0f;
        }
        if (target == null) {
            return 0.0f;
        }
        return stack.itematic$getBehavior(ItemComponentTypes.POINTABLE)
            .map(PointableItemComponent::pointsTo)
            .map(RegistryEntry::value)
            .map(p -> this.getAngle(p.createPos(stack, world, target), target, world))
            .orElse(0.0f);
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.itematic$hasBehavior(ItemComponentTypes.POINTABLE);
    }

    private float getAngle(GlobalPos pos, Entity target, World world) {
        if (this.canPointTo(target, pos)) {
            return this.getAngle(pos.pos(), target, world);
        }
        return AIMLESS_INTERPOLATOR.update(world, world.getRandom().nextFloat());
    }

    private boolean canPointTo(Entity target, GlobalPos pos) {
        return pos != null
            && pos.dimension() == target.getWorld().getRegistryKey()
            && pos.pos().getSquaredDistance(target.getPos()) >= DISTANCE_EPSILON;
    }

    private float getAngle(BlockPos pos, Entity target, World world) {
        float entityAngle = this.getNormalizedAngle(target, pos);
        float entityRotation = this.getNormalizedEntityRotation(target);
        float interpolatedAngle = INTERPOLATOR.update(world, 0.5f - (entityRotation - 0.25f));
        return MathHelper.floorMod(entityAngle + interpolatedAngle, 1.0f);
    }

    private float getNormalizedAngle(Entity target, BlockPos pos) {
        Vec3d centerPos = Vec3d.ofCenter(pos);
        return (float) (Math.atan2( centerPos.getZ() - target.getZ(), centerPos.getX() - target.getX()) / (2.0d * Math.PI));
    }

    private float getNormalizedEntityRotation(Entity target) {
        return MathHelper.floorMod(target.getBodyYaw() / 360.0f, 1.0f);
    }

    public static class Interpolator {
        private final float interpolationFactor;
        private long lastTick;
        private float value;
        private float speed;

        public Interpolator(float interpolationFactor) {
            this.interpolationFactor = interpolationFactor;
        }

        public float update(World world, float angle) {
            long currentTick = world.getTime();
            if (currentTick == this.lastTick) {
                return this.value;
            }

            this.lastTick = currentTick;
            float deltaValue = MathHelper.floorMod(angle - this.value + 0.5f, 1.0f) - 0.5f;
            this.speed += deltaValue * 0.1f;
            this.speed *= this.interpolationFactor;
            return this.value = MathHelper.floorMod(this.value + this.speed, 1.0f);
        }
    }
}
