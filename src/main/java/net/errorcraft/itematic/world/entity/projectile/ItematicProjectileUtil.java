package net.errorcraft.itematic.world.entity.projectile;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public class ItematicProjectileUtil {
    private ItematicProjectileUtil() {}

    public static InteractionHand getWeaponHoldingHand(LivingEntity entity, ResourceKey<Item> key) {
        return entity.getMainHandItem().itematic$isOf(key)
            ? InteractionHand.MAIN_HAND
            : InteractionHand.OFF_HAND;
    }
}
