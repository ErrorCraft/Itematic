package net.errorcraft.itematic.item.model.override;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface ModelOverride {
    float apply(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed);
    default boolean isApplicable(ItemStack stack) {
        return true;
    }
}
