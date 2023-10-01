package net.errorcraft.itematic.item.model.override;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface ClampedModelOverride extends ModelOverride {
    default float apply(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        return MathHelper.clamp(this.applyUnclamped(stack, world, target, seed), 0.0f, 1.0f);
    }

    float applyUnclamped(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed);
}
