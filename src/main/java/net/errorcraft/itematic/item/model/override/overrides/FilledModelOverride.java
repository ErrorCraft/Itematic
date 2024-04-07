package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.model.override.ClampedModelOverride;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FilledModelOverride implements ClampedModelOverride {
    @Override
    public float applyUnclamped(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        return stack.itematic$getComponent(ItemComponentTypes.ITEM_HOLDER)
            .map(c -> c.fullness(stack).floatValue())
            .orElse(0.0f);
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.itematic$hasComponent(ItemComponentTypes.ITEM_HOLDER);
    }
}
