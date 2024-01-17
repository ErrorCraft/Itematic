package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CastModelOverride implements ModelOverride {
    @Override
    public float apply(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        if (target == null) {
            return 0.0f;
        }
        if (!target.itematic$isHolding(stack)) {
            return 0.0f;
        }
        if (!(target instanceof PlayerEntity player)) {
            return 0.0f;
        }
        if (player.fishHook == null) {
            return 0.0f;
        }
        return 1.0f;
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.itematic$isOf(ItemKeys.FISHING_ROD);
    }
}
