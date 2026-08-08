package net.errorcraft.itematic.mixin.entity.ai.goal;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.world.entity.projectile.ItematicProjectileUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RangedBowAttackGoal.class)
public class BowAttackGoalExtender {
    @Redirect(
        method = "isHoldingBow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/Monster;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingForBowUseRegistryKeyCheck(Monster instance, Item item) {
        return instance.itematic$isHolding(ItemKeys.BOW);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getWeaponHoldingHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/InteractionHand;"
        )
    )
    private InteractionHand getHandPossiblyHoldingForBowUseRegistryKey(LivingEntity entity, Item item) {
        return ItematicProjectileUtil.getWeaponHoldingHand(entity, ItemKeys.BOW);
    }
}
