package net.errorcraft.itematic.mixin.entity.ai.goal;

import net.errorcraft.itematic.entity.projectile.ItematicProjectileUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BowAttackGoal.class)
public class BowAttackGoalExtender {
    @Redirect(
        method = "isHoldingBow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/HostileEntity;isHolding(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isHoldingForBowUseRegistryKeyCheck(HostileEntity instance, Item item) {
        return instance.itematic$isHolding(ItemKeys.BOW);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;"
        )
    )
    private Hand getHandPossiblyHoldingForBowUseRegistryKey(LivingEntity entity, Item item) {
        return ItematicProjectileUtil.getHandPossiblyHolding(entity, ItemKeys.BOW);
    }
}
