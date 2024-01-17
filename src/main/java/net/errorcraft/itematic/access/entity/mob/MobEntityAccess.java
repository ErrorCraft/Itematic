package net.errorcraft.itematic.access.entity.mob;

import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.minecraft.item.ItemStack;

public interface MobEntityAccess {
    default boolean itematic$trySetBaby(boolean baby) {
        return false;
    }
    default boolean itematic$canUseShooter(ItemStack stack, ShooterItemComponent component) {
        return false;
    }
}
