package net.errorcraft.itematic.mixin.world.entity.ai.goal;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.entity.projectile.Projectiles;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.Direct;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RangedBowAttackGoal.class)
public class RangedBowAttackGoalExtender {
    @WrapOperation(
        method = "isHoldingBow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/Monster;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingBowUseDataComponent(Monster instance, Item item, Operation<Boolean> original) {
        return instance.isHolding(stack -> stack.get(ItematicDataComponents.SHOOTER_METHOD) == Direct.INSTANCE);
    }

    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getWeaponHoldingHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/InteractionHand;"
        )
    )
    private InteractionHand getWeaponHoldingHandForBowUseDataComponent(LivingEntity mob, Item weaponItem, Operation<InteractionHand> original) {
        return Projectiles.getWeaponHoldingHand(mob, Direct.INSTANCE);
    }
}
