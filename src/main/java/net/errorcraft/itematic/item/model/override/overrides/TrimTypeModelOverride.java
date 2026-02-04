package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.model.override.ClampedModelOverride;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TrimTypeModelOverride implements ClampedModelOverride {
    @Override
    public float applyUnclamped(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        ArmorTrim trim = stack.get(DataComponentTypes.TRIM);
        if (trim == null) {
            return 0.0f;
        }

        return trim.material().value().itemModelIndex();
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.contains(DataComponentTypes.TRIM);
    }
}
