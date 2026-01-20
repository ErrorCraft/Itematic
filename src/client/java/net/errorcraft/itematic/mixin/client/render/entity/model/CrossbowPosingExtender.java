package net.errorcraft.itematic.mixin.client.render.entity.model;

import net.errorcraft.itematic.item.shooter.method.methods.ChargeableShooterMethod;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrossbowPosing.class)
public class CrossbowPosingExtender {
    @Redirect(
        method = "charge",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/entity/LivingEntity;)I"
        )
    )
    private static int getPullTimeUseItemComponent(LivingEntity user) {
        return ChargeableShooterMethod.getChargeTime(user.getActiveItem(), user);
    }
}
