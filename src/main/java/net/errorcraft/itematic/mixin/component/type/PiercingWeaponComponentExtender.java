package net.errorcraft.itematic.mixin.component.type;

import net.minecraft.component.type.PiercingWeaponComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PiercingWeaponComponent.class)
public class PiercingWeaponComponentExtender {
    @Redirect(
        method = "stab",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
        )
    )
    private double useCustomAttackDamage(LivingEntity instance, RegistryEntry<EntityAttribute> attribute) {
        return instance.itematic$getAttackDamage();
    }
}
