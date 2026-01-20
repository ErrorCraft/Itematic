package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.entity.projectile.ItematicProjectileUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.shooter.method.methods.ChargeableShooterMethod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.CrossbowAttackTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrossbowAttackTask.class)
public class CrossbowAttackTaskExtender {
    @Redirect(
        method = {
            "shouldRun(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;)Z",
            "finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;J)V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/MobEntity;isHolding(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isHoldingForCrossbowUseRegistryKeyCheck(MobEntity instance, Item item) {
        return instance.itematic$isHolding(ItemKeys.CROSSBOW);
    }

    @Redirect(
        method = "tickState",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;"
        )
    )
    private Hand getHandPossiblyHoldingForCrossbowUseRegistryKey(LivingEntity entity, Item item) {
        return ItematicProjectileUtil.getHandPossiblyHolding(entity, ItemKeys.CROSSBOW);
    }

    @Redirect(
        method = "tickState",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/entity/LivingEntity;)I"
        )
    )
    private int getPullTimeUseItemComponent(LivingEntity user, @Local ItemStack usedStack) {
        return ChargeableShooterMethod.getChargeTime(usedStack, user);
    }
}
