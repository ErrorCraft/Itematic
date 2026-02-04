package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DamagedModelOverride implements ModelOverride {
    @Override
    public float apply(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        return stack.isDamaged() ? 1.0f : 0.0f;
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.itematic$hasBehavior(ItemComponentTypes.DAMAGEABLE);
    }
}
