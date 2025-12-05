package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuardianEntity.class)
public abstract class GuardianEntityExtender extends MobEntityExtender {
    protected GuardianEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.GUARDIAN_SPAWN_EGG;
    }

    @Mixin(targets = "net/minecraft/entity/mob/GuardianEntity$FireBeamGoal")
    public static class FireBeamGoalExtender {
        @Redirect(
            method = "tick",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/entity/mob/GuardianEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
            )
        )
        private double useCustomAttackDamage(GuardianEntity instance, RegistryEntry<EntityAttribute> attribute) {
            return instance.itematic$getAttackDamage();
        }
    }
}
