package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.model.override.ClampedModelOverride;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PullModelOverride implements ClampedModelOverride {
    @Override
    public float applyUnclamped(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        if (target == null) {
            return 0.0f;
        }

        if (target.getActiveItem() != stack) {
            return 0.0f;
        }

        return stack.itematic$getComponent(ItemComponentTypes.SHOOTER)
            .map(shooter -> shooter.method().pullProgress(stack, target, target.itematic$itemUsedTicks()))
            .orElse(0.0f);
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.itematic$hasComponent(ItemComponentTypes.SHOOTER);
    }
}
