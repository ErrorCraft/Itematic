package net.errorcraft.itematic.mixin.world.item.component;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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
public class KineticWeaponExtender {
    @Definition(id = "stack", local = @Local(type = ItemStack.class, argsOnly = true))
    @Definition(id = "getUseDuration", method = "Lnet/minecraft/world/item/ItemStack;getUseDuration(Lnet/minecraft/world/entity/LivingEntity;)I")
    @Definition(id = "user", local = @Local(type = LivingEntity.class, argsOnly = true))
    @Definition(id = "ticksRemaining", local = @Local(type = int.class, argsOnly = true))
    @Expression("stack.getUseDuration(user) - ticksRemaining")
    @ModifyExpressionValue(
        method = "damageEntities",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private int useUsedTicks(int original, @Local(argsOnly = true) int ticksRemaining) {
        // This parameter has been repurposed to be the used ticks rather than the remaining ticks due to indefinite use durations
        return ticksRemaining;
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
