package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TimeModelOverride implements ModelOverride {
    private static final AngleModelOverride.Interpolator INTERPOLATOR = new AngleModelOverride.Interpolator(0.9f);

    @Override
    public float apply(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        if (world == null) {
            return 0.0f;
        }
        float angle = this.getAngle(world);
        return INTERPOLATOR.update(world, angle);
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.itematic$isOf(ItemKeys.CLOCK);
    }

    private float getAngle(World world) {
        if (world.getDimension().natural()) {
            return world.getSkyAngle(1.0f);
        }
        return world.getRandom().nextFloat();
    }
}
