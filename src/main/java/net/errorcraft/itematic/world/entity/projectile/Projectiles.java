package net.errorcraft.itematic.world.entity.projectile;

import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethod;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public class Projectiles {
    private Projectiles() {}

    public static InteractionHand getWeaponHoldingHand(LivingEntity entity, ShooterMethod<?> method) {
        return entity.getMainHandItem().get(ItematicDataComponents.SHOOTER_METHOD) == method
            ? InteractionHand.MAIN_HAND
            : InteractionHand.OFF_HAND;
    }
}
