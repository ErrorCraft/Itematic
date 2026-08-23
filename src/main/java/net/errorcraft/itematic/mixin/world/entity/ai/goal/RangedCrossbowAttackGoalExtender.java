package net.errorcraft.itematic.mixin.world.entity.ai.goal;

import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.entity.projectile.ItematicProjectileUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RangedCrossbowAttackGoal.class)
public class RangedCrossbowAttackGoalExtender {
    @Redirect(
        method = "isHoldingCrossbow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/Monster;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingCrossbowCheckId(Monster instance, Item item) {
        return instance.itematic$isHolding(ItemIds.CROSSBOW);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getWeaponHoldingHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/InteractionHand;"
        )
    )
    private InteractionHand getHandForHeldCrossbowUseId(LivingEntity mob, Item weaponItem) {
        return ItematicProjectileUtil.getWeaponHoldingHand(mob, ItemIds.CROSSBOW);
    }
}
