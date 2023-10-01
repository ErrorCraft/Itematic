package net.errorcraft.itematic.entity.projectile;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Hand;

public class ItematicProjectileUtil {
    private ItematicProjectileUtil() {}

    public static Hand getHandPossiblyHolding(LivingEntity entity, RegistryKey<Item> key) {
        return entity.getMainHandStack().isOf(key) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }
}
