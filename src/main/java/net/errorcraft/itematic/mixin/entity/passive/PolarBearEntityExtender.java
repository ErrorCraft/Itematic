package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PolarBearEntity.class)
public abstract class PolarBearEntityExtender extends MobEntityExtender {
    protected PolarBearEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "tryAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/PolarBearEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
        )
    )
    private double useCustomAttackDamage(PolarBearEntity instance, RegistryEntry<EntityAttribute> attribute) {
        return this.itematic$getAttackDamage();
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.POLAR_BEAR_SPAWN_EGG;
    }
}
