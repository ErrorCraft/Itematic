package net.errorcraft.itematic.mixin.component.type;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.KineticWeapon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KineticWeapon.class)
public class KineticWeaponComponentExtender {
    @Redirect(
        method = "damageEntities",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getUseDuration(Lnet/minecraft/world/entity/LivingEntity;)I"
        )
    )
    private int useDoubleUsedTicks(ItemStack instance, LivingEntity user, @Local(argsOnly = true) int remainingUseTicks) {
        return 2 * remainingUseTicks;
    }

    @Redirect(
        method = "damageEntities",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeBaseValue(Lnet/minecraft/core/Holder;)D"
        )
    )
    private double useCustomBaseAttackDamage(LivingEntity instance, Holder<Attribute> attribute) {
        return instance.itematic$getBaseAttackDamage();
    }
}
