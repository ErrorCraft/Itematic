package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LeftHandedModelOverride implements ModelOverride {
    @Override
    public float apply(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        return MathHelper.clamp((float)stack.getDamage() / stack.getMaxDamage(), 0.0f, 1.0f);
    }
}
