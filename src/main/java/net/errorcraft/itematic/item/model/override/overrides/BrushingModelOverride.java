package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BrushItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BrushingModelOverride implements ModelOverride {
    private static final float DURATION = BrushItem.ANIMATION_DURATION;

    @Override
    public float apply(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        if (target == null) {
            return 0.0f;
        }
        if (target.getActiveItem() != stack) {
            return 0.0f;
        }
        return (target.getItemUseTimeLeft() % DURATION) / DURATION;
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.itematic$isOf(ItemKeys.BRUSH);
    }
}
