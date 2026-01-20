package net.errorcraft.itematic.mixin.entity.ai.goal;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.entity.projectile.ItematicProjectileUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.shooter.method.methods.ChargeableShooterMethod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrossbowAttackGoal.class)
public class CrossbowAttackGoalExtender {
    @Redirect(
        method = "isEntityHoldingCrossbow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/HostileEntity;isHolding(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isHoldingForCrossbowUseRegistryKeyCheck(HostileEntity instance, Item item) {
        return instance.itematic$isHolding(ItemKeys.CROSSBOW);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;"
        )
    )
    private Hand getHandPossiblyHoldingForCrossbowUseRegistryKey(LivingEntity entity, Item item) {
        return ItematicProjectileUtil.getHandPossiblyHolding(entity, ItemKeys.CROSSBOW);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/entity/LivingEntity;)I"
        )
    )
    private int getPullTimeUseItemComponent(LivingEntity user, @Local ItemStack usedStack) {
        return ChargeableShooterMethod.getChargeTime(usedStack, user);
    }
}
