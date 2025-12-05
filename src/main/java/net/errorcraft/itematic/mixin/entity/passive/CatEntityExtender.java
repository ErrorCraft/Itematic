package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CatEntity.class)
public abstract class CatEntityExtender extends MobEntityExtender {
    protected CatEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getAttackDamage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/CatEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
        )
    )
    private double useCustomAttackDamage(CatEntity instance, RegistryEntry<EntityAttribute> attribute) {
        return this.itematic$getAttackDamage();
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.CAT_SPAWN_EGG;
    }
}
