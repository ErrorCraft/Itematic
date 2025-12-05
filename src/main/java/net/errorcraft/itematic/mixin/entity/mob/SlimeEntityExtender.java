package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityExtender extends MobEntityExtender {
    protected SlimeEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getDamageAmount",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/SlimeEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
        )
    )
    private double useCustomAttackDamage(SlimeEntity instance, RegistryEntry<EntityAttribute> attribute) {
        return this.itematic$getAttackDamage();
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.SLIME_SPAWN_EGG;
    }
}
