package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import net.minecraft.entity.ai.brain.task.RamImpactTask;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RamImpactTask.class)
public class RamImpactTaskExtender {
    @Redirect(
        method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/GoatEntity;J)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/GoatEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
        )
    )
    private double useCustomAttackDamage(GoatEntity instance, RegistryEntry<EntityAttribute> attribute) {
        return instance.itematic$getAttackDamage();
    }
}
