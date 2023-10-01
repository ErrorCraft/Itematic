package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FireworkModelOverride implements ModelOverride {
    @Override
    public float apply(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        if (world == null) {
            return 0.0f;
        }
        return stack.getComponent(ItemComponentTypes.SHOOTER)
            .map(c -> c.isCharged(stack) && c.hasLoadedAmmunition(stack, world.getRegistryManager(), ItemKeys.FIREWORK_ROCKET) ? 1.0f : 0.0f)
            .orElse(0.0f);
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.hasComponent(ItemComponentTypes.SHOOTER);
    }
}
