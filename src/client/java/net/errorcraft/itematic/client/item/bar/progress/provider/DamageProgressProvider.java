package net.errorcraft.itematic.client.item.bar.progress.provider;

import net.errorcraft.itematic.client.item.bar.progress.ProgressProvider;
import net.errorcraft.itematic.client.item.bar.progress.ProgressProviderKeys;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class DamageProgressProvider implements ProgressProvider {
    @Override
    public Identifier id() {
        return ProgressProviderKeys.DAMAGE;
    }

    @Override
    public boolean isVisible(ItemStack stack) {
        return stack.isDamaged();
    }

    @Override
    public float get(ItemStack stack) {
        return (float) stack.getDamage() / stack.getMaxDamage();
    }
}
