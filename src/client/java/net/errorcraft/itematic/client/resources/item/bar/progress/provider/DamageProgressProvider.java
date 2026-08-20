package net.errorcraft.itematic.client.resources.item.bar.progress.provider;

import net.errorcraft.itematic.client.resources.item.bar.progress.ProgressProvider;
import net.minecraft.world.item.ItemStack;

public class DamageProgressProvider implements ProgressProvider {
    @Override
    public boolean isVisible(ItemStack stack) {
        return stack.isDamaged();
    }

    @Override
    public float get(ItemStack stack) {
        return (float) stack.getDamageValue() / stack.getMaxDamage();
    }
}
