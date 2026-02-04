package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ChargedModelOverride implements ModelOverride {
    @Override
    public float apply(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        if (CrossbowItem.isCharged(stack)) {
            return 1.0f;
        }

        return 0.0f;
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.itematic$hasBehavior(ItemComponentTypes.SHOOTER);
    }
}
