package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.model.override.ClampedModelOverride;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CooldownModelOverride implements ClampedModelOverride {
    @Override
    public float applyUnclamped(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        if (target instanceof PlayerEntity player) {
            player.getItemCooldownManager().getCooldownProgress(stack, 0.0f);
        }

        return 0.0f;
    }
}
