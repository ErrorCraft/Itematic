package net.errorcraft.itematic.mixin.entity.projectile.thrown;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.SplashPotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SplashPotionEntity.class)
public abstract class SplashPotionEntityExtender extends ThrownItemEntityExtender {
    protected SplashPotionEntityExtender(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected RegistryKey<Item> getDefaultItemKey() {
        return ItemKeys.SPLASH_POTION;
    }
}
