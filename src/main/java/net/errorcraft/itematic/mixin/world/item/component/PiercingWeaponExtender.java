package net.errorcraft.itematic.mixin.world.item.component;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.component.PiercingWeapon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PiercingWeapon.class)
public class PiercingWeaponExtender {
    @Redirect(
        method = "attack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D"
        )
    )
    private double useCustomAttackDamage(LivingEntity instance, Holder<Attribute> attribute) {
        return instance.itematic$getAttackDamage();
    }
}
