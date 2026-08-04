package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.behavior.RamTarget;
import net.minecraft.world.entity.animal.goat.Goat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RamTarget.class)
public class RamImpactTaskExtender {
    @Redirect(
        method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/goat/Goat;J)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/goat/Goat;getAttributeValue(Lnet/minecraft/core/Holder;)D"
        )
    )
    private double useCustomAttackDamage(Goat instance, Holder<Attribute> attribute) {
        return instance.itematic$getAttackDamage();
    }
}
