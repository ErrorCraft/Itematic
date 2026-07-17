package net.errorcraft.itematic.mixin.component.type;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.type.KineticWeaponComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KineticWeaponComponent.class)
public class KineticWeaponComponentExtender {
    @Redirect(
        method = "usageTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getMaxUseTime(Lnet/minecraft/entity/LivingEntity;)I"
        )
    )
    private int useDoubleUsedTicks(ItemStack instance, LivingEntity user, @Local(argsOnly = true) int remainingUseTicks) {
        return 2 * remainingUseTicks;
    }

    @Redirect(
        method = "usageTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;getAttributeBaseValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
        )
    )
    private double useCustomBaseAttackDamage(LivingEntity instance, RegistryEntry<EntityAttribute> attribute) {
        return instance.itematic$getBaseAttackDamage();
    }
}
