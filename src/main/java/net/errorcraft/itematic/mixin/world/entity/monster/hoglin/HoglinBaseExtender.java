package net.errorcraft.itematic.mixin.world.entity.monster.hoglin;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HoglinBase.class)
public interface HoglinBaseExtender {
    @Redirect(
        method = "hurtAndThrowTarget",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D"
        )
    )
    private static double useCustomAttackDamage(LivingEntity instance, Holder<Attribute> attribute) {
        return instance.itematic$getAttackDamage();
    }
}
