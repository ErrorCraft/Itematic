package net.errorcraft.itematic.mixin.world.entity.ai.behavior;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.entity.projectile.Projectiles;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.Chargeable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowAttack.class)
public class CrossbowAttackExtender {
    @WrapOperation(
        method = {
            "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;)Z",
            "stop(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;J)V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Mob;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingCrossbowUseDataComponent(Mob instance, Item item, Operation<Boolean> original) {
        return instance.isHolding(stack -> stack.get(ItematicDataComponents.SHOOTER_METHOD) == Chargeable.INSTANCE);
    }

    @WrapOperation(
        method = "crossbowAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getWeaponHoldingHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/InteractionHand;"
        )
    )
    private InteractionHand getWeaponHoldingHandForCrossbowUseDataComponent(LivingEntity mob, Item weaponItem, Operation<InteractionHand> original) {
        return Projectiles.getWeaponHoldingHand(mob, Chargeable.INSTANCE);
    }
}
