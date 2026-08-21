package net.errorcraft.itematic.mixin.world.entity.ai.behavior;

import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.entity.projectile.ItematicProjectileUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrossbowAttack.class)
public class CrossbowAttackExtender {
    @Redirect(
        method = {
            "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;)Z",
            "stop(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;J)V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Mob;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingCrossbowCheckId(Mob instance, Item item) {
        return instance.itematic$isHolding(ItemIds.CROSSBOW);
    }

    @Redirect(
        method = "crossbowAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getWeaponHoldingHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/InteractionHand;"
        )
    )
    private InteractionHand getHandForHeldCrossbowUseId(LivingEntity entity, Item item) {
        return ItematicProjectileUtil.getWeaponHoldingHand(entity, ItemIds.CROSSBOW);
    }
}
