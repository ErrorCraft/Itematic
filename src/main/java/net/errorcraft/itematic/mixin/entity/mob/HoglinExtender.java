package net.errorcraft.itematic.mixin.entity.mob;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.mob.Hoglin;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Hoglin.class)
public interface HoglinExtender {
    @Redirect(
        method = "tryAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
        )
    )
    private static double useCustomAttackDamage(LivingEntity instance, RegistryEntry<EntityAttribute> attribute) {
        return instance.itematic$getAttackDamage();
    }
}
