package net.errorcraft.itematic.mixin.entity.projectile.thrown;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrownSplashPotion.class)
public abstract class SplashPotionEntityExtender extends ThrownItemEntityExtender {
    protected SplashPotionEntityExtender(EntityType<? extends ThrowableProjectile> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected ResourceKey<Item> getDefaultItemKey() {
        return ItemKeys.SPLASH_POTION;
    }
}
